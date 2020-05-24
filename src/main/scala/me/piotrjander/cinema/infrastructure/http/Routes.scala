package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class Routes(screeningController: ScreeningController, reservationController: ReservationController) {

  val routes: Route =
    pathPrefix("screenings") {
      pathEnd {
        get {
          screeningController.list()
        }
      } ~
      path(Segment) { screeningId =>
        get {
          screeningController.describe(screeningId)
        }
      }
    } ~
    pathPrefix("reservations") {
      pathEnd {
        post {
          reservationController.create()
        }
      } ~
      pathPrefix(Segment) { reservationId =>
        path("confirm") {
          post {
            reservationController.confirm(reservationId)
          }
        }
      }
    }
}
