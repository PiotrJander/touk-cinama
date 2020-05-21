package me.piotrjander.cinema.infrastructure.database.inmemory

import cats.effect.Sync
import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{Reservation, ReservationId, ScreeningId}
import me.piotrjander.cinema.domain.repository.ReservationRepository
import me.piotrjander.cinema.infrastructure.database.model

/**
 * TODO validation?
 */
class ReservationDatabaseView[F[_]: Sync](db: UnderlyingDatabase) extends ReservationRepository[F] {

  override def create(reservation: entity.Reservation): F[Unit] = Sync[F].delay {
    val id = ReservationId(db.getNextId)
    val entity.Reservation(_, screening, name, ticketsBreakdown, seats, confirmed) = reservation
    val reservationModel = model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
    db.reservations += (id -> reservationModel)
  }

  override def list(screening: ScreeningId): F[Seq[Reservation]] = Sync[F].delay {
    db.reservations.values.filter(_.screening == screening).toSeq
  }

  override def get(id: ReservationId): F[Option[entity.Reservation]] = Sync[F].delay {
    db.reservations.get(id).map(r => db.getReservationEntity(r))
  }

  override def update(id: ReservationId, reservation: entity.Reservation): F[Unit] = Sync[F].delay {
      val entity.Reservation(Some(id), screening, name, ticketsBreakdown, seats, confirmed) =
        reservation
      val reservationModel =
        model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
      db.reservations += (id -> reservationModel)
  }

  override def delete(id: ReservationId): F[Unit] = Sync[F].delay {
    db.reservations -= id
    if (db.reservationRequests.contains(id)) {
      db.reservationRequests -= id
    }
  }
}
