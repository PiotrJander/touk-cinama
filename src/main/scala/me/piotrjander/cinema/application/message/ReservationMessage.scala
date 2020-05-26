package me.piotrjander.cinema.application.message

import me.piotrjander.cinema.application.EntityPayloads.{Reservation, ReservationRequest}
import me.piotrjander.cinema.domain.entity.{Seat, TicketsBreakdown}

object ReservationMessage {

  case class CreateRequest(screeningId: String,
                           name: String,
                           ticketsBreakdown: TicketsBreakdown,
                           seats: Seq[Seat])

  case class CreateResponse(confirmation: ReservationRequest)

  case class ConfirmRequest(reservationId: String, secret: String)

  case class ConfirmResponse(reservation: Reservation)

}
