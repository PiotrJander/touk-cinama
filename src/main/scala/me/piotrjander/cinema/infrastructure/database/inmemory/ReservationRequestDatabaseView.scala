package me.piotrjander.cinema.infrastructure.database.inmemory

import cats.Id
import me.piotrjander.cinema.domain.entity.{ReservationId, ReservationRequest, ReservationRequestSecret}
import me.piotrjander.cinema.domain.repository.ReservationRequestRepository

class ReservationRequestDatabaseView(db: UnderlyingDatabase) extends ReservationRequestRepository[Id] {

  override def create(reservationRequest: ReservationRequest): Id[Unit] = ???

  override def listExpired(): Id[Seq[ReservationRequest]] = ???

  override def delete(id: ReservationId): Id[Unit] = ???
}
