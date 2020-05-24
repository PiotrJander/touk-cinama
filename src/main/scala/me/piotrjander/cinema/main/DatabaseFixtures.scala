package me.piotrjander.cinema.main

import java.time.Duration

import me.piotrjander.cinema.domain.entity.{
  ConfirmationSecret,
  FullName,
  Movie,
  ReservationId,
  ScreeningRoom,
  Seat,
  TicketsBreakdown
}
import me.piotrjander.cinema.infrastructure.database.inmemory.InMemoryDatabase
import me.piotrjander.cinema.infrastructure.database.model.{
  Reservation,
  ReservationRequest,
  Screening
}

object DatabaseFixtures {

  def populate(database: InMemoryDatabase): Unit = {

    val referenceTime = Configuration.REFERENCE_TIME

    val movies = Vector(
      Movie(1, "Gone with the Wind"),
      Movie(2, "Breakfast at Tiffany's"),
      Movie(3, "Citizen Kane")
    )
    movies.foreach(database.addMovie)

    val row = Vector("1", "2", "3")
    val rowNames = Vector("I", "II", "III")
    val seats =
      rowNames.foldRight(Map.empty[String, Seq[String]]) { (rowName, acc) =>
        acc + (rowName -> row)
      }
    val rooms = Vector(
      ScreeningRoom(1, "Room 1", seats),
      ScreeningRoom(2, "Room 2", seats),
      ScreeningRoom(3, "Room 3", seats)
    )
    rooms.foreach(database.addScreeningRoom)

    val screenings = Vector(
      Screening(
        1,
        movies(0).id.get,
        rooms(0).id.get,
        referenceTime.plus(Duration.ofHours(1))
      ),
      Screening(
        2,
        movies(1).id.get,
        rooms(0).id.get,
        referenceTime.plus(Duration.ofHours(2))
      ),
      Screening(
        3,
        movies(1).id.get,
        rooms(1).id.get,
        referenceTime.plus(Duration.ofHours(3))
      ),
      Screening(
        4,
        movies(2).id.get,
        rooms(1).id.get,
        referenceTime.plus(Duration.ofHours(0))
      ),
      Screening(
        5,
        movies(0).id.get,
        rooms(2).id.get,
        referenceTime.plus(Duration.ofHours(1))
      ),
      Screening(
        6,
        movies(2).id.get,
        rooms(2).id.get,
        referenceTime.plus(Duration.ofHours(2))
      )
    )
    screenings.foreach(database.addScreening)

    val reservations = Vector(
      Reservation(
        ReservationId("1"),
        screenings.head.id,
        FullName("John", "Doe"),
        TicketsBreakdown(1, 1, 0),
        Vector(Seat("I", "1"), Seat("I", "2")),
        confirmed = true
      ),
      Reservation(
        ReservationId("2"),
        screenings.head.id,
        FullName("Jane", "Doe"),
        TicketsBreakdown(1, 0, 1),
        Vector(Seat("III", "2"), Seat("III", "3")),
        confirmed = false
      ),
      Reservation(
        ReservationId("3"),
        screenings.head.id,
        FullName("Adam", "Smith"),
        TicketsBreakdown(1, 0, 0),
        Vector(Seat("II", "2")),
        confirmed = false
      ),
    )
    reservations.foreach(database.addReservation)

    val reservationRequests = Vector(
      ReservationRequest(
        reservations(1).id,
        ConfirmationSecret("foo"),
        referenceTime.plus(Duration.ofHours(1))
      ),
      ReservationRequest(
        reservations(2).id,
        ConfirmationSecret("bar"),
        referenceTime.minus(Duration.ofHours(1))
      )
    )
    reservationRequests.foreach(database.addReservationRequest)
  }
}
