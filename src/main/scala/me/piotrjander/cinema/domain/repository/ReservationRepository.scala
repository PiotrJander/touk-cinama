package me.piotrjander.cinema.domain.repository

import me.piotrjander.cinema.domain.entity.Reservation

trait ReservationRepository[F[_]] {

  def get(id: String): F[Option[Reservation]]

}
