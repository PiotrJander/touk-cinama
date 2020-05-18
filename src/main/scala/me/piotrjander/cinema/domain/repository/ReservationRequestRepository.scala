package me.piotrjander.cinema.domain.repository

import me.piotrjander.cinema.domain.entity.{ReservationId, ReservationRequest}

trait ReservationRequestRepository[F[_]] {

  def create(reservationRequest: ReservationRequest): F[Unit]

  def listExpired(): F[Seq[ReservationRequest]]

  def delete(id: ReservationId): F[Unit]

}
