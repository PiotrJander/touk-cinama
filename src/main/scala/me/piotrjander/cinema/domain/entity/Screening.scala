package me.piotrjander.cinema.domain.entity

import java.time.LocalDateTime

case class Screening(id: String, movie: Movie, room: ScreeningRoom, dateTime: LocalDateTime)
