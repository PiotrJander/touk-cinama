package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity.{FullName, ReservationId, ScreeningId, Seat, TicketsBreakdown}

case class Reservation(id: ReservationId,
                       screening: ScreeningId,
                       name: FullName,
                       ticketsBreakdown: TicketsBreakdown,
                       seats: Seq[Seat],
                       confirmed: Boolean)
