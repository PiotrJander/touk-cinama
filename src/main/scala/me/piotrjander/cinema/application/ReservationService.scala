package me.piotrjander.cinema.application

import cats.effect.Async
import me.piotrjander.cinema.domain.repository._
import me.piotrjander.cinema.application.ReservationMessages._

class ReservationService[F[_]: Async](
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F]
) extends ReservationServiceApi[F] {

  override def create(request: CreateRequest): F[CreateResponse] = ???

  override def confirm(request: ConfirmRequest): F[ConfirmResponse] = ???
}
