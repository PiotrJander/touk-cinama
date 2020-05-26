package me.piotrjander.cinema.main

import java.time.{Duration, LocalDateTime}

import me.piotrjander.cinema.domain.entity.TicketPrices

object Configuration {
  val REFERENCE_TIME: LocalDateTime = LocalDateTime.of(1970, 1, 1, 0, 0, 0)
  val RESERVATION_TIMEOUT: Duration = Duration.ofMinutes(15)
  val RESERVATION_BEFORE_START: Duration = Duration.ofMinutes(15)

  val TICKET_PRICES: TicketPrices = TicketPrices(25, 18, 12.50f)

  val HOST = "0.0.0.0"
  val PORT = 8080
}
