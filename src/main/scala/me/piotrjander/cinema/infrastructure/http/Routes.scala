package me.piotrjander.cinema.infrastructure.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

class Routes(screeningController: ScreeningController, reservationController: ReservationController) {

  val routes: Route =
    rejectEmptyResponse {
      pathPrefix("screenings") {
        pathEnd {
          get {
            parameters(("from", "to")) { (startDateTime, endDateTime) =>
              screeningController.list(startDateTime, endDateTime)
            }
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
}
