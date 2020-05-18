package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

case class ScreeningId(id: String) extends AnyVal

case class Screening(id: ScreeningId,
                     movie: MovieId,
                     room: ScreeningRoomId,
                     dateTime: LocalDateTime)
