package me.piotrjander.cinema.application.service

import cats.{Applicative, ApplicativeError}
import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.EntityPayloads
import me.piotrjander.cinema.application.message.ReservationMessage._
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.provider.{
  ConfirmationSecretGenerator,
  LocalClock,
  SeatAvailability
}
import me.piotrjander.cinema.application.validator.{
  FullNameValidator,
  TicketsBreakdownValidator
}
import me.piotrjander.cinema.domain.entity.{
  Reservation,
  ReservationId,
  ReservationRequest,
  Screening,
  ScreeningId
}
import me.piotrjander.cinema.domain.repository._
import me.piotrjander.cinema.main.Configuration

class ReservationService[F[_]: Async](
  screeningRepository: ScreeningRepository[F],
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F],
  confirmationSecretGenerator: ConfirmationSecretGenerator[F],
  seatAvailability: SeatAvailability[F]
) extends ReservationServiceApi[F] {

  private def getValidScreening(screeningId: ScreeningId): F[Screening] =
    for {
      maybeScreening <- screeningRepository.get(screeningId)
      screening <- ApplicativeError
        .liftFromOption[F](maybeScreening, new BadRequestException("Screening not found"))
      dateTimeNow <- localClock.dateTimeNow()
      errorCondition = dateTimeNow
        .plus(Configuration.RESERVATION_BEFORE_START)
        .isAfter(screening.dateTime)
      _ <- Applicative[F].whenA(errorCondition) {
        Async[F].raiseError(new BadRequestException("Ticket sales for this screening have ended"))
      }
    } yield screening

  override def create(request: CreateRequest): F[CreateResponse] =
    for {
      // validate request
      screening <- getValidScreening(ScreeningId(request.screeningId))
      name <- new FullNameValidator[F]().parse(request.name)
      _ <- new TicketsBreakdownValidator[F]()
        .validate(request.ticketsBreakdown, request.seats)
      _ <- seatAvailability.validateSeatSelection(screening, request.seats)

      // create reservation
      reservation = Reservation(
        None,
        screening,
        name,
        request.ticketsBreakdown,
        request.seats,
        confirmed = false
      )
      createdReservation <- reservationRepository.create(reservation)

      // create reservation request
      dateTimeNow <- localClock.dateTimeNow()
      confirmationSecret <- confirmationSecretGenerator.generate
      reservationRequest = ReservationRequest(
        createdReservation,
        confirmationSecret,
        dateTimeNow
      )
      _ <- reservationRequestRepository.create(reservationRequest)
    } yield {
      val payload =
        EntityPayloads.ReservationRequest.fromEntity(reservationRequest)
      CreateResponse(payload)
    }

  private def getReservation(
    reservationId: ReservationId
  ): F[(Reservation, ReservationRequest)] =
    for {
      maybeReservationRequest <- reservationRequestRepository.get(reservationId)
      reservationRequest <- ApplicativeError
        .liftFromOption[F](maybeReservationRequest, new BadRequestException("Reservation request not found"))
      reservation <- reservationRepository.get(reservationId).map(_.get)
    } yield (reservation, reservationRequest)

  private def validateConfirmationRequest(
    reservationRequest: ReservationRequest,
    requestSecret: String
  ): F[Unit] =
    for {
      dateTimeNow <- localClock.dateTimeNow()
      reservationExpired = reservationRequest.isExpired(dateTimeNow)
      reservationSecretMatches = reservationRequest.confirmationSecret
        .equalsString(requestSecret)
      _ <- Applicative[F].whenA(reservationExpired) {
        Async[F].raiseError(new BadRequestException("Reservation expired"))
      }
      _ <- Applicative[F].whenA(!reservationSecretMatches) {
        Async[F].raiseError(new BadRequestException("Wrong secret key"))
      }
    } yield ()

  override def confirm(request: ConfirmRequest): F[ConfirmResponse] = {
    val reservationId = ReservationId(request.reservationId)
    for {
      result <- getReservation(reservationId)
      (reservation, reservationRequest) = result
      _ <- validateConfirmationRequest(reservationRequest, request.secret)
      _ <- reservationRequestRepository.delete(reservationId)
      updatedReservation = reservation.copy(confirmed = true)
      _ <- reservationRepository.update(reservationId, updatedReservation)
    } yield {
      val reservationPayload =
        EntityPayloads.Reservation.fromEntity(updatedReservation)
      ConfirmResponse(reservationPayload)
    }
  }
}
