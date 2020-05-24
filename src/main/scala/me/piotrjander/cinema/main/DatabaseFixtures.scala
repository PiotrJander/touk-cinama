package me.piotrjander.cinema.main

import me.piotrjander.cinema.domain.entity.{Movie, ScreeningRoom}
import me.piotrjander.cinema.infrastructure.database.inmemory.InMemoryDatabase

object DatabaseFixtures {

  def populate(database: InMemoryDatabase): Unit = {

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

    ???
  }
}
