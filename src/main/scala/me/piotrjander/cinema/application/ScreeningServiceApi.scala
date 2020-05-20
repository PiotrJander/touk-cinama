package me.piotrjander.cinema.application

import me.piotrjander.cinema.application.ScreeningMessages._

trait ScreeningServiceApi[F[_]] {

  def list(request: ListRequest): F[ListResponse]

  def get(screeningId: GetRequest): F[Option[GetResponse]]
}
