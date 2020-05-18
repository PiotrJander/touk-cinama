package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity

case class MovieId(id: String) extends AnyVal

case class Movie(id: MovieId, title: String) {

  def toEntity: entity.Movie = entity.Movie(title)
}
