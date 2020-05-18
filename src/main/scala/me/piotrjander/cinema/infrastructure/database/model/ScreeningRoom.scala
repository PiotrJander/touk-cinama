package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity

case class ScreeningRoomId(id: String) extends AnyVal

case class ScreeningRoom(id: ScreeningRoomId,
                         name: String,
                         seats: Seq[Seq[entity.Seat]]) {

  def toEntity: entity.ScreeningRoom = entity.ScreeningRoom(name, seats)
}
