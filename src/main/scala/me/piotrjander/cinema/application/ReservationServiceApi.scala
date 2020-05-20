package me.piotrjander.cinema.application

import me.piotrjander.cinema.application.ReservationMessages._

trait ReservationServiceApi[F[_]] {

  def create(request: CreateRequest): F[CreateResponse]

  def confirm(request: ConfirmRequest): F[ConfirmResponse]
}
