package me.piotrjander.cinema.infrastructure.database.inmemory

import cats.effect.Sync
import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{Reservation, ReservationId, ScreeningId}
import me.piotrjander.cinema.domain.repository.ReservationRepository
import me.piotrjander.cinema.infrastructure.database.model

class ReservationDatabaseView[F[_]: Sync](database: InMemoryDatabase) extends ReservationRepository[F] {

  override def create(reservation: entity.Reservation): F[Reservation] = Sync[F].delay {
    val id = ReservationId(database.getNextId)
    val entity.Reservation(_, screening, name, ticketsBreakdown, seats, confirmed) = reservation
    val reservationModel = model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
    database.reservations += (id -> reservationModel)
    reservation.copy(id = Some(id))
  }

  override def list(screening: ScreeningId): F[Seq[Reservation]] = Sync[F].delay {
    database.reservations.values.filter(_.screening == screening).map(r => database.getReservationEntity(r)).toSeq
  }

  override def get(id: ReservationId): F[Option[entity.Reservation]] = Sync[F].delay {
    database.reservations.get(id).map(r => database.getReservationEntity(r))
  }

  override def update(id: ReservationId, reservation: entity.Reservation): F[Unit] = Sync[F].delay {
      val entity.Reservation(Some(id), screening, name, ticketsBreakdown, seats, confirmed) = reservation
      val reservationModel = model.Reservation(id, screening.id.get, name, ticketsBreakdown, seats, confirmed)
      database.reservations += (id -> reservationModel)
  }

  override def delete(id: ReservationId): F[Unit] = Sync[F].delay {
    database.reservations -= id
    if (database.reservationRequests.contains(id)) {
      database.reservationRequests -= id
    }
  }
}
