package me.piotrjander.cinema.domain.entity

case class MovieId(id: String) extends AnyVal

case class Movie(id: Option[MovieId], title: String)

object Movie {

  def apply(title: String): Movie = Movie(None, title)

  def apply(id: MovieId, title: String): Movie = Movie(Some(id), title)

  def apply(id: String, title: String): Movie = Movie(MovieId(id), title)

  def apply(id: Int, title: String): Movie = Movie(id.toString, title)
}
