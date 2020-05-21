package me.piotrjander.cinema.application.provider

import java.time.Duration

import me.piotrjander.cinema.application.EntityPayloads
import me.piotrjander.cinema.domain.entity.{ReservationRequest, TicketPrices}

class ReservationRequestEncoder(reservationTimeout: Duration,
                                reservationBeforeStart: Duration,
                                ticketPrices: TicketPrices)
    extends EntityPayloads.ReservationRequestEncoder {
  override def fromEntity(
    e: ReservationRequest
  ): EntityPayloads.ReservationRequest = {
    val expirationTime =
      Seq(
        e.submittedTime.plus(reservationTimeout),
        e.reservation.screening.dateTime.minus(reservationBeforeStart)
      ).min
    EntityPayloads.ReservationRequest(
      EntityPayloads.Reservation.fromEntity(e.reservation),
      e.confirmationSecret.toString,
      expirationTime.toString,
      ticketPrices.amountToPay(e.reservation.ticketsBreakdown)
    )
  }
}
