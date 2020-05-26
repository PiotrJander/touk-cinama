package me.piotrjander.cinema.application.provider

import me.piotrjander.cinema.domain.entity.{Screening, ScreeningSeatAvailability, Seat}

trait SeatAvailability[F[_]] {

  def getAvailableSeats(screening: Screening): F[ScreeningSeatAvailability]

  def validateSeatSelection(screening: Screening, seats: Seq[Seat]): F[Unit]
}
