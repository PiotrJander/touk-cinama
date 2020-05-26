package me.piotrjander.cinema.domain.entity

case class ReservationId(id: String) extends AnyVal

case class Reservation(id: Option[ReservationId],
                       screening: Screening,
                       name: FullName,
                       ticketsBreakdown: TicketsBreakdown,
                       seats: Seq[Seat],
                       confirmed: Boolean)
