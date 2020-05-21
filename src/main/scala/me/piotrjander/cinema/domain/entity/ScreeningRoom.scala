package me.piotrjander.cinema.domain.entity

case class ScreeningRoomId(id: String) extends AnyVal

case class ScreeningRoom(id: Option[ScreeningRoomId],
                         name: String,
                         seats: Map[String, Seq[String]])
