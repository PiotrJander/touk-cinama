package me.piotrjander.cinema.application.service

import me.piotrjander.cinema.application.message.ReservationMessage.{ConfirmRequest, ConfirmResponse, CreateRequest, CreateResponse}

trait ReservationServiceApi[F[_]] {

  def create(request: CreateRequest): F[CreateResponse]

  def confirm(request: ConfirmRequest): F[ConfirmResponse]
}
