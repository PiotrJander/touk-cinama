package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.{MovieId, ScreeningId, ScreeningRoomId}

case class Screening(id: ScreeningId,
                     movie: MovieId,
                     room: ScreeningRoomId,
                     dateTime: LocalDateTime)

object Screening {
  def apply(id: Int, movie: MovieId, room: ScreeningRoomId, dateTime: LocalDateTime): Screening =
    Screening(ScreeningId(id.toString), movie, room, dateTime)
}