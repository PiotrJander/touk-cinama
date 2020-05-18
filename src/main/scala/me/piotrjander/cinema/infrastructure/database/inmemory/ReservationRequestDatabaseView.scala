package me.piotrjander.cinema.infrastructure.database.inmemory

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.ReservationId
import me.piotrjander.cinema.domain.repository.ReservationRequestRepository
import me.piotrjander.cinema.infrastructure.database.model

import scala.util.Try

class ReservationRequestDatabaseView(db: UnderlyingDatabase)
    extends ReservationRequestRepository[Try] {

  override def create(
    reservationRequest: entity.ReservationRequest
  ): Try[Unit] = Try {
    val entity.ReservationRequest(reservation, secret, submittedTime) =
      reservationRequest
    if (!db.reservations.contains(reservation.id.get)) {
      throw new BadDatabaseRequest("Associated reservation doesn't exist")
    }
    val reservationRequestModel =
      model.ReservationRequest(reservation.id.get, secret, submittedTime)
    db.reservationRequests += (reservation.id.get -> reservationRequestModel)
  }

  override def list(
    beforeTime: LocalDateTime
  ): Try[Seq[entity.ReservationRequest]] = Try {
    db.reservationRequests.values
      .filter(rr => rr.submittedTime.isBefore(beforeTime))
      .map(rr => {
        val model.ReservationRequest(id, secret, submittedTime) = rr
        val reservation = db.getReservationEntity(db.reservations(id))
        entity.ReservationRequest(reservation, secret, submittedTime)
      })
      .toSeq
  }

  override def delete(id: ReservationId): Try[Unit] = Try {
    if (db.reservationRequests.contains(id)) {
      db.reservationRequests -= id
    } else {
      throw new BadDatabaseRequest("Not found")
    }
  }
}
