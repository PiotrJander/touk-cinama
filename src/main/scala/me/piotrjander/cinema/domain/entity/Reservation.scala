package me.piotrjander.cinema.domain.entity

case class Reservation(id: String,
                       screening: Screening,
                       name: FullName,
                       adultTickets: Int,
                       studentTickets: Int,
                       childTickets: Int,
                       seats: Seq[Seat],
                       confirmed: Boolean)
