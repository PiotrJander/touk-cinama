package me.piotrjander.cinema.application

import me.piotrjander.cinema.domain.entity

object EntityPayloads {

  case class Movie(id: String, title: String)

  object Movie {
    def fromEntity(e: entity.Movie): Movie =
      Movie(e.id.get.id, e.title)
  }

  case class ScreeningRoom(id: String, name: String, seats: Seq[Seq[String]])

  object ScreeningRoom {
    def fromEntity(e: entity.ScreeningRoom): ScreeningRoom =
      ScreeningRoom(e.id.get.id, e.name, e.seats.map(_.map(_.name)))
  }

  case class Screening(id: String,
                       movie: Movie,
                       screening: ScreeningRoom,
                       dateTime: String)

  object Screening {
    def fromEntity(e: entity.Screening): Screening =
      Screening(
        e.id.get.id,
        Movie.fromEntity(e.movie),
        ScreeningRoom.fromEntity(e.room),
        e.dateTime.toString
      )
  }

  case class Reservation(id: String,
                         screening: Screening,
                         name: String,
                         ticketsBreakdown: entity.TicketsBreakdown,
                         seats: Seq[String],
                         confirmed: Boolean)

  case class ReservationRequest(reservation: Reservation,
                                secret: String,
                                submittedTime: String,
                                expirationTime: String)
}
