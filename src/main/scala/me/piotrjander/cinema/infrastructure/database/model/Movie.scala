package me.piotrjander.cinema.infrastructure.database.model

import me.piotrjander.cinema.domain.entity

case class Movie (id: String, title: String) {

  def toEntity: entity.Movie = entity.Movie(title)
}
