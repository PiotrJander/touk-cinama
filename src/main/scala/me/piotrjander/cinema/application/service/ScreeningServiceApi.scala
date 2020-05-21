package me.piotrjander.cinema.application.service

import me.piotrjander.cinema.application.message.ScreeningMessage.{GetRequest, GetResponse, ListRequest, ListResponse}

trait ScreeningServiceApi[F[_]] {

  def list(request: ListRequest): F[ListResponse]

  def get(screeningId: GetRequest): F[Option[GetResponse]]
}
