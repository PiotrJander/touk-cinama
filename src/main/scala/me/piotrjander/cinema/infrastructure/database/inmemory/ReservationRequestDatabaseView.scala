package me.piotrjander.cinema.infrastructure.database.inmemory

import java.time.LocalDateTime

import cats.effect.Sync
import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.ReservationId
import me.piotrjander.cinema.domain.repository.ReservationRequestRepository
import me.piotrjander.cinema.infrastructure.database.model

class ReservationRequestDatabaseView[F[_]: Sync](db: UnderlyingDatabase)
    extends ReservationRequestRepository[F] {

  val F: Sync[F] = implicitly[Sync[F]]

  override def create(
    reservationRequest: entity.ReservationRequest
  ): F[Unit] = F.delay {
    val entity.ReservationRequest(reservation, secret, submittedTime) =
      reservationRequest
    val reservationRequestModel =
      model.ReservationRequest(reservation.id.get, secret, submittedTime)
    db.reservationRequests += (reservation.id.get -> reservationRequestModel)
  }

  override def list(
    beforeTime: LocalDateTime
  ): F[Seq[entity.ReservationRequest]] = F.delay {
    db.reservationRequests.values
      .filter(rr => rr.submittedTime.isBefore(beforeTime))
      .map(rr => {
        val reservation = db.getReservationEntity(db.reservations(rr.reservation))
        entity.ReservationRequest(reservation, rr.requestSecret, rr.submittedTime)
      })
      .toSeq
  }

  override def delete(id: ReservationId): F[Unit] = F.delay {
    db.reservationRequests -= id
  }
}
