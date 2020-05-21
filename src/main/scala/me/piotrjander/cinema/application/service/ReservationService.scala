package me.piotrjander.cinema.application.service

import java.time.Duration

import cats.{Applicative, ApplicativeError}
import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.EntityPayloads
import me.piotrjander.cinema.application.message.ReservationMessage._
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.provider.{ConfirmationSecretGenerator, LocalClock, SeatAvailability}
import me.piotrjander.cinema.application.validator.{FullNameValidator, TicketsBreakdownValidator}
import me.piotrjander.cinema.domain.entity.{Reservation, ReservationRequest, Screening, ScreeningId}
import me.piotrjander.cinema.domain.repository._

class ReservationService[F[_]: Async](
  screeningRepository: ScreeningRepository[F],
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F],
  confirmationSecretGenerator: ConfirmationSecretGenerator[F],
  reservationRequestEncoder: EntityPayloads.ReservationRequestEncoder,
  beforeMovieStarts: Duration,
  seatAvailability: SeatAvailability[F]
) extends ReservationServiceApi[F] {

  private def getValidScreening(screeningId: ScreeningId): F[Screening] =
    for {
      maybeScreening <- screeningRepository.get(screeningId)
      screening <- ApplicativeError.liftFromOption[F](maybeScreening, new BadRequestException())
      dateTimeNow <- localClock.dateTimeNow()
      _ <- Applicative[F].whenA(dateTimeNow.plus(beforeMovieStarts).isAfter(screening.dateTime)) {
        Async[F].raiseError(new BadRequestException())
      }
    } yield screening

  override def create(request: CreateRequest): F[CreateResponse] = {

    for {
      // validate request
      screening <- getValidScreening(ScreeningId(request.screeningId))
      name <- new FullNameValidator[F]().parse(request.name)
      _ <- new TicketsBreakdownValidator[F]().validate(request.ticketsBreakdown, request.seats)
      // TODO validate seats

      reservation =
        Reservation(None, screening, name, request.ticketsBreakdown, request.seats, confirmed = false)
      createdReservation <- reservationRepository.create(reservation)
      dateTimeNow <- localClock.dateTimeNow()
      confirmationSecret <- confirmationSecretGenerator.generate
      reservationRequest =
        ReservationRequest(createdReservation, confirmationSecret, dateTimeNow)
      _ <- reservationRequestRepository.create(reservationRequest)
    } yield {
      val payload = reservationRequestEncoder.fromEntity(reservationRequest)
      CreateResponse(payload)
    }
  }

  override def confirm(request: ConfirmRequest): F[ConfirmResponse] = ???
}
