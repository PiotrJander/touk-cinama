package me.piotrjander.cinema.domain.repository

import me.piotrjander.cinema.domain.entity.{Reservation, ReservationId, ScreeningId}

trait ReservationRepository[F[_]] {

  def create(reservation: Reservation): F[Reservation]

  def list(screening: ScreeningId): F[Seq[Reservation]]

  def get(id: ReservationId): F[Option[Reservation]]

  def update(id: ReservationId, reservation: Reservation): F[Unit]

  def delete(id: ReservationId): F[Unit]

}
