package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity

case class ScreeningRoom(id: String, name: String, seats: Seq[Seq[entity.Seat]]) {

  def toEntity: entity.ScreeningRoom = entity.ScreeningRoom(name, seats)
}
