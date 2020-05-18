package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity.Seat

case class ReservationId(id: String) extends AnyVal

case class Reservation(id: ReservationId,
                       screening: ScreeningId,
                       adultTickets: Int,
                       studentTickets: Int,
                       childTickets: Int,
                       seats: Seq[Seat],
                       confirmed: Boolean)
