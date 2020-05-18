package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.ReservationId
import me.piotrjander.cinema.domain.repository.ReservationRepository
import me.piotrjander.cinema.infrastructure.database.model

import scala.util.Try

/**
 * TODO validation?
 */
class ReservationDatabaseView(db: UnderlyingDatabase) extends ReservationRepository[Try] {

  override def create(reservation: entity.Reservation): Try[Unit] = Try {
    val id = ReservationId(db.getNextId)
    val entity.Reservation(_, screening, name, ticketsBreakdown, seats, confirmed) = reservation
    if (!db.screenings.contains(screening.id.get)) {
      throw new BadDatabaseRequest("Associated screening does not exist")
    }
    val reservationModel = model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
    db.reservations += (id -> reservationModel)
  }

  override def get(id: ReservationId): Try[Option[entity.Reservation]] = Try {
    db.reservations.get(id).map(r => db.getReservationEntity(r))
  }

  override def update(id: ReservationId, reservation: entity.Reservation): Try[Unit] = Try {
    if (db.reservations.contains(id)) {
      val entity.Reservation(Some(id), screening, name, ticketsBreakdown, seats, confirmed) = reservation
      if (!db.screenings.contains(screening.id.get)) {
        throw new BadDatabaseRequest("Associated screening does not exist")
      }
      val reservationModel = model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
      db.reservations += (id -> reservationModel)
    } else {
      throw new BadDatabaseRequest("Reservation doesn't exist")
    }
  }

  override def delete(id: ReservationId): Try[Unit] = Try {
    if (db.reservations.contains(id)) {
      db.reservations -= id
      if (db.reservationRequests.contains(id)) {
        db.reservationRequests -= id
      }
    } else {
      throw new BadDatabaseRequest("Reservation doesn't exist")
    }
  }

}
