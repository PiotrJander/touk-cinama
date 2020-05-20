package me.piotrjander.cinema.application

import EntityPayloads.ReservationRequest
import EntityPayloads.Reservation
import me.piotrjander.cinema.domain.entity.TicketsBreakdown

object ReservationMessages {

  case class CreateRequest(screeningId: String,
                           name: String,
                           ticketsBreakdown: TicketsBreakdown,
                           seats: Seq[String])

  case class CreateResponse(confirmation: ReservationRequest)

  case class ConfirmRequest(reservationId: String, secret: String)

  case class ConfirmResponse(reservation: Reservation)

}
