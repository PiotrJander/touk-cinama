package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity.Seat

case class Reservation(id: String,
                       screeningId: String,
                       adultTickets: Int,
                       studentTickets: Int,
                       childTickets: Int,
                       seats: Seq[Seat],
                       confirmed: Boolean)
