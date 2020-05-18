package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

case class Screening(id: String, movieId: String, roomId: String, dateTime: LocalDateTime)
