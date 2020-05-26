package me.piotrjander.cinema.domain.repository

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.{ReservationId, ReservationRequest}

trait ReservationRequestRepository[F[_]] {

  def create(reservationRequest: ReservationRequest): F[Unit]

  def list(beforeTime: LocalDateTime): F[Seq[ReservationRequest]]

  def get(id: ReservationId): F[Option[ReservationRequest]]

  def delete(id: ReservationId): F[Unit]

}
