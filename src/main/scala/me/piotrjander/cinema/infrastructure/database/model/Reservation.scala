package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity.{ReservationId, ScreeningId, Seat}

case class Reservation(id: ReservationId,
                       screening: ScreeningId,
                       adultTickets: Int,
                       studentTickets: Int,
                       childTickets: Int,
                       seats: Seq[Seat],
                       confirmed: Boolean)
