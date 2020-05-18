package me.piotrjander.cinema.domain.repository

import me.piotrjander.cinema.domain.entity.{Reservation, ReservationRequest, ReservationRequestSecret}

trait ReservationRequestRepository[F[_]] {

  def create(reservationRequest: ReservationRequest): F[Unit]

  def confirm(reservation: Reservation, secret: ReservationRequestSecret): F[Unit]

  def cleanUp(): F[Unit]

}
