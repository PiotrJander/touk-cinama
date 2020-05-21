package me.piotrjander.cinema.application

import cats.ApplicativeError
import cats.implicits._
import cats.effect.Async
import me.piotrjander.cinema.domain.repository._
import me.piotrjander.cinema.application.ReservationMessages._
import me.piotrjander.cinema.domain.entity.{Reservation, ScreeningId}

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
//      reservation = Reservation(None, screening, )
//      foo <- reservationRepository.create()
    } yield ???
  }

  override def confirm(request: ConfirmRequest): F[ConfirmResponse] = ???
}
