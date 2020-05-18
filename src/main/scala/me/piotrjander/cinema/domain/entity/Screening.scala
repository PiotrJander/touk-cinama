package me.piotrjander.cinema.domain.entity

import java.time.LocalDateTime

case class ScreeningId(id: String) extends AnyVal

case class Screening(id: Option[ScreeningId], movie: Movie, room: ScreeningRoom, dateTime: LocalDateTime)
