package me.piotrjander.cinema.domain.entity

case class ScreeningRoomId(id: String) extends AnyVal

case class ScreeningRoom(id: ScreeningRoomId,
                         name: String,
                         seats: Seq[Seq[Seat]])
