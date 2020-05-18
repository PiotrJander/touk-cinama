package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity.{Reservation, ReservationId}
import me.piotrjander.cinema.domain.repository.ReservationRepository

import scala.util.Try

/**
 * TODO validation?
 */
class ReservationDatabaseView(db: UnderlyingDatabase) extends ReservationRepository[Try] {

  // TODO create and update validation:

  override def create(reservation: Reservation): Try[Unit] = Try {
    db.reservations += (reservation.id -> reservation)
  }

  override def get(id: ReservationId): Try[Option[Reservation]] = Try {
    db.reservations.get(id).map(r => db.getReservationEntity(r))
  }

  override def update(id: ReservationId, reservation: Reservation): Try[Unit] = Try {
    if (db.reservations.contains(id)) {
      db.reservations += (id -> reservation)
    } else {
      throw new BadDatabaseRequest("Reservation doesn't exist")
    }
  }

  override def delete(id: ReservationId): Try[Unit] = Try {
    if (db.reservations.contains(id)) {
      db.reservations -= id // TODO also delete request
    } else {
      throw new BadDatabaseRequest("Reservation doesn't exist")
    }
  }

}
