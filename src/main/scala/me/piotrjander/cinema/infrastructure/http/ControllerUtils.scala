package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, completeOrRecoverWith}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.CompleteOrRecoverWithMagnet
import me.piotrjander.cinema.application.exception.BadRequestException

object ControllerUtils {

  def completeOrBadRequest(magnet: CompleteOrRecoverWithMagnet): Route =
    completeOrRecoverWith(magnet) {
      case e: BadRequestException =>
        complete((StatusCodes.BadRequest, e.getMessage))
    }
}
