package me.piotrjander.cinema.application.message

import me.piotrjander.cinema.application.EntityPayloads.{Reservation, ReservationRequest}
import me.piotrjander.cinema.domain.entity.TicketsBreakdown

object ReservationMessage {

  case class CreateRequest(screeningId: String,
                           name: String,
                           ticketsBreakdown: TicketsBreakdown,
                           seats: Seq[String])

  case class CreateResponse(confirmation: ReservationRequest)

  case class ConfirmRequest(reservationId: String, secret: String)

  case class ConfirmResponse(reservation: Reservation)

}
