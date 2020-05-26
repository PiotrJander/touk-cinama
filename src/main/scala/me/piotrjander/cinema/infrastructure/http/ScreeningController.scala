package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.server.Directives.rejectEmptyResponse
import akka.http.scaladsl.server.Route
import cats.effect.IO
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.service.ScreeningServiceApi

class ScreeningController(screeningService: ScreeningServiceApi[IO]) {

  def list(startDateTime: String, endDateTime: String): Route = {
    val request = ListRequest(startDateTime, endDateTime)
    val future = screeningService.list(request).unsafeToFuture()
    rejectEmptyResponse {
      ControllerUtils.completeOrBadRequest(future)
    }
  }

  def describe(screeningId: String): Route = {
    val request = DescribeRequest(screeningId)
    val future = screeningService.describe(request).unsafeToFuture()
    ControllerUtils.completeOrBadRequest(future)
  }
}
