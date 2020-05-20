package me.piotrjander.cinema.application

import me.piotrjander.cinema.domain.entity.TicketsBreakdown

object EntityPayloads {

  case class Movie(id: String, title: String)

  case class ScreeningRoom(id: String, name: String, seats: Seq[Seq[String]])

  case class Screening(id: String,
                       movie: Movie,
                       screening: Screening,
                       dateTime: String,
                       url: String)

  case class Reservation(id: String,
                         screening: Screening,
                         name: String,
                         ticketsBreakdown: TicketsBreakdown,
                         seats: Seq[String],
                         confirmed: Boolean)

  case class ReservationRequest(reservation: Reservation,
                                secret: String,
                                submittedTime: String,
                                expirationTime: String)
}
