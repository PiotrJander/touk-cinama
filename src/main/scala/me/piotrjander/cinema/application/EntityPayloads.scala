package me.piotrjander.cinema.application

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{ScreeningSeatAvailability, Seat}

object EntityPayloads {

  case class Movie(id: String, title: String)

  object Movie {
    def fromEntity(e: entity.Movie): Movie =
      Movie(e.id.get.id, e.title)
  }

  case class ScreeningRoom(id: String, name: String, seats: Map[String, Seq[String]])

  object ScreeningRoom {
    def fromEntity(e: entity.ScreeningRoom): ScreeningRoom =
      ScreeningRoom(e.id.get.id, e.name, e.seats)
  }

  case class Screening(id: String,
                       movie: Movie,
                       roomName: String,
                       seatAvailability: Map[String, Map[String, Boolean]],
                       dateTime: String)

  object Screening {
    def fromEntity(e: entity.Screening, availability: ScreeningSeatAvailability): Screening =
      Screening(
        e.id.get.id,
        Movie.fromEntity(e.movie),
        e.room.name,
        availability.seats,
        e.dateTime.toString
      )
  }

  case class Reservation(id: String,
                         screening: Screening,
                         name: String,
                         ticketsBreakdown: entity.TicketsBreakdown,
                         seats: Seq[Seat],
                         confirmed: Boolean)

  case class ReservationRequest(reservation: Reservation,
                                secret: String,
                                submittedTime: String,
                                expirationTime: String)
}
