package me.piotrjander.cinema.infrastructure.database.inmemory

import cats.Id
import me.piotrjander.cinema.domain.entity.{Reservation, ReservationId}
import me.piotrjander.cinema.domain.repository.ReservationRepository

class ReservationDatabaseView(db: UnderlyingDatabase) extends ReservationRepository[Id] {

  override def create(reservation: Reservation): Id[Unit] = ???

  override def get(id: ReservationId): Id[Option[Reservation]] = ???

  override def update(id: ReservationId, reservation: Reservation): Id[Unit] = ???

  override def delete(id: ReservationId): Id[Unit] = ???

}
