package me.piotrjander.cinema.main

import java.time.Duration

import me.piotrjander.cinema.domain.entity.{Movie, ScreeningRoom}
import me.piotrjander.cinema.infrastructure.database.inmemory.InMemoryDatabase
import me.piotrjander.cinema.infrastructure.database.model.Screening

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
      rowNames.foldRight(Map.empty[String, Seq[String]]){ (rowName, acc) => acc + (rowName -> row) }
    val rooms = Vector(
      ScreeningRoom(1, "Room 1", seats),
      ScreeningRoom(2, "Room 2", seats),
      ScreeningRoom(3, "Room 3", seats)
    )
    rooms.foreach(database.addScreeningRoom)

    val screenings = Vector(
      Screening(1, movies(0).id.get, rooms(0).id.get, referenceTime.plus(Duration.ofHours(1))),
      Screening(1, movies(1).id.get, rooms(0).id.get, referenceTime.plus(Duration.ofHours(2))),
      Screening(1, movies(1).id.get, rooms(1).id.get, referenceTime.plus(Duration.ofHours(3))),
      Screening(1, movies(2).id.get, rooms(1).id.get, referenceTime.plus(Duration.ofHours(0))),
      Screening(1, movies(0).id.get, rooms(2).id.get, referenceTime.plus(Duration.ofHours(1))),
      Screening(1, movies(2).id.get, rooms(2).id.get, referenceTime.plus(Duration.ofHours(2)))
    )
    screenings.foreach(database.addScreening)
  }
}
