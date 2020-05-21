package me.piotrjander.cinema.application

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{ScreeningSeatAvailability, Seat}

object EntityPayloads {

  case class Movie(id: String, title: String)

  object Movie {
    def fromEntity(e: entity.Movie): Movie =
      Movie(e.id.get.id, e.title)
  }

  case class ScreeningRoom(id: String,
                           name: String,
                           seats: Map[String, Seq[String]])

  object ScreeningRoom {
    def fromEntity(e: entity.ScreeningRoom): ScreeningRoom =
      ScreeningRoom(e.id.get.id, e.name, e.seats)
  }

  case class ScreeningSummary(id: String,
                              movie: Movie,
                              roomName: String,
                              dateTime: String)

  object ScreeningSummary {
    def fromEntity(e: entity.Screening): ScreeningSummary =
      ScreeningSummary(
        e.id.get.id,
        Movie.fromEntity(e.movie),
        e.room.name,
        e.dateTime.toString
      )

    implicit val ordering: Ordering[ScreeningSummary] =
      Ordering.by[ScreeningSummary, String](_.movie.title)
        .orElse(Ordering.by[ScreeningSummary, String](_.dateTime))
  }

  case class Screening(summary: ScreeningSummary,
                       seatAvailability: Map[String, Map[String, Boolean]])

  object Screening {
    def fromEntity(e: entity.Screening,
                   availability: ScreeningSeatAvailability): Screening =
      Screening(ScreeningSummary.fromEntity(e), availability.seats)
  }

  case class Reservation(id: String,
                         screening: ScreeningSummary,
                         name: String,
                         ticketsBreakdown: entity.TicketsBreakdown,
                         seats: Seq[Seat],
                         confirmed: Boolean)

  object Reservation {
    def fromEntity(e: entity.Reservation): Reservation =
      Reservation(
        e.id.get.id,
        ScreeningSummary.fromEntity(e.screening),
        e.name.toString,
        e.ticketsBreakdown,
        e.seats,
        e.confirmed
      )
  }

  case class ReservationRequest(reservation: Reservation,
                                secret: String,
                                expirationTime: String,
                                amountToPay: Float)

  trait ReservationRequestEncoder {
    def fromEntity(
      e: entity.ReservationRequest
    ): ReservationRequest
  }
}
