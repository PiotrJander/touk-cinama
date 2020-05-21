package me.piotrjander.cinema.application.service

import cats.ApplicativeError
import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.LocalClock
import me.piotrjander.cinema.application.message.ReservationMessage._
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.validator.{FullNameValidator, TicketsBreakdownValidator}
import me.piotrjander.cinema.domain.entity.ScreeningId
import me.piotrjander.cinema.domain.repository._

class ReservationService[F[_]: Async](
  screeningRepository: ScreeningRepository[F],
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F]
) extends ReservationServiceApi[F] {

  val F: Async[F] = implicitly[Async[F]]

  def screeningNotFoundError = new BadRequestException("Screening not found")

  override def create(request: CreateRequest): F[CreateResponse] = {

    for {
      maybeScreening <- screeningRepository.get(ScreeningId(request.screeningId))
      screening <- ApplicativeError.liftFromOption[F](maybeScreening, screeningNotFoundError)
      name <- new FullNameValidator[F]().parse(request.name)
      _ <- new TicketsBreakdownValidator[F]().validate(request.ticketsBreakdown)
//      reservation = Reservation(None, screening, name, request.ticketsBreakdown, )
//      foo <- reservationRepository.create()
    } yield ???
  }

  override def confirm(request: ConfirmRequest): F[ConfirmResponse] = ???
}
