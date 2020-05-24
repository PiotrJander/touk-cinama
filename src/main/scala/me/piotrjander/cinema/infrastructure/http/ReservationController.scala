package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import cats.effect.IO
import me.piotrjander.cinema.application.message.ReservationMessage._
import me.piotrjander.cinema.application.service.ReservationServiceApi
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class ReservationController(reservationService: ReservationServiceApi[IO]) {

  import ReservationController._

  def create(): Route = {
    entity(as[CreateRequest]) { payload =>
      val future = reservationService.create(payload).unsafeToFuture()
      ControllerUtils.completeOrBadRequest(future)
    }
  }

  def confirm(reservationId: String): Route = {
    entity(as[ConfirmPayload]) { payload =>
      val request = ConfirmRequest(reservationId, payload.secret)
      val future = reservationService.confirm(request).unsafeToFuture()
      ControllerUtils.completeOrBadRequest(future)
    }
  }
}

object ReservationController {
  case class ConfirmPayload(secret: String)
}
