package me.piotrjander.cinema.application.service

import me.piotrjander.cinema.application.message.ScreeningMessage.{DescribeRequest, GetResponse, ListRequest, ListResponse}

trait ScreeningServiceApi[F[_]] {

  def list(request: ListRequest): F[ListResponse]

  def describe(screeningId: DescribeRequest): F[Option[GetResponse]]
}
