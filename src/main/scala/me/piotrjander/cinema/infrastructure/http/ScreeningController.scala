package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.server.Route
import cats.effect.{Async, IO}
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.service.ScreeningServiceApi
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class ScreeningController(screeningService: ScreeningServiceApi[IO]) {

  def list(startDateTime: String, endDateTime: String): Route = {
    val future = screeningService.list(ListRequest(startDateTime, endDateTime)).unsafeToFuture()
    ControllerUtils.completeOrBadRequest(future)
  }

  def describe(screeningId: String): Route = {
    val future = screeningService.describe(DescribeRequest(screeningId)).unsafeToFuture()
    ControllerUtils.completeOrBadRequest(future)
  }
}
