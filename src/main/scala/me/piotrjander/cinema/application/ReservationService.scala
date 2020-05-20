package me.piotrjander.cinema.application

import cats.effect.Async
import me.piotrjander.cinema.domain.repository._

class ReservationService[F[_]: Async](
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F]
) extends ReservationServiceApi[F] {

  override def create(request: ReservationMessages.CreateRequest): F[ReservationMessages.CreateResponse] = ???

  override def confirm(request: ReservationMessages.ConfirmRequest): F[ReservationMessages.ConfirmResponse] = ???
}
