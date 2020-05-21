package me.piotrjander.cinema.domain.entity

case class ScreeningSeatAvailability(seats: Map[String, Map[String, Boolean]]) {

  def isReserved(seat: Seat): Boolean = isReserved(seat.row, seat.name)

  def isReserved(row: String, name: String): Boolean = seats(row)(name)
}
