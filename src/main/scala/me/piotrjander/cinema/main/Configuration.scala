package me.piotrjander.cinema.main

import java.time.Duration

import me.piotrjander.cinema.domain.entity.TicketPrices

object Configuration {
  val RESERVATION_TIMEOUT: Duration = Duration.ofMinutes(15)
  val RESERVATION_BEFORE_START: Duration = Duration.ofMinutes(15)
  val TICKET_PRICES: TicketPrices = TicketPrices(25, 18, 12.50f)
}
