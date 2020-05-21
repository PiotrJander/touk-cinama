package me.piotrjander.cinema.application.service

import cats.ApplicativeError
import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.{ConfirmationSecretGenerator, EntityPayloads, LocalClock}
import me.piotrjander.cinema.application.message.ReservationMessage._
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.validator.{FullNameValidator, TicketsBreakdownValidator}
import me.piotrjander.cinema.domain.entity.{Reservation, ReservationRequest, ScreeningId}
import me.piotrjander.cinema.domain.repository._

class ReservationService[F[_]: Async](
  screeningRepository: ScreeningRepository[F],
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F],
  confirmationSecretGenerator: ConfirmationSecretGenerator[F],
  reservationRequestEncoder: EntityPayloads.ReservationRequestEncoder
) extends ReservationServiceApi[F] {

  val F: Async[F] = implicitly[Async[F]]

  def screeningNotFoundError = new BadRequestException("Screening not found")

  override def create(request: CreateRequest): F[CreateResponse] = {

    for {
      maybeScreening <- screeningRepository.get(ScreeningId(request.screeningId))
      screening <- ApplicativeError.liftFromOption[F](maybeScreening, screeningNotFoundError)
      name <- new FullNameValidator[F]().parse(request.name)
      _ <- new TicketsBreakdownValidator[F]().validate(request.ticketsBreakdown)
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
