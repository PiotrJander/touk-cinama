package me.piotrjander.cinema.domain.entity

case class ReservationId(id: String) extends AnyVal

case class Reservation(id: ReservationId,
                       screening: Screening,
                       name: FullName,
                       adultTickets: Int,
                       studentTickets: Int,
                       childTickets: Int,
                       seats: Seq[Seat],
                       confirmed: Boolean)
