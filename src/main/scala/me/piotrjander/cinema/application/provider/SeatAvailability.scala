package me.piotrjander.cinema.application.provider

import me.piotrjander.cinema.domain.entity.{Screening, ScreeningSeatAvailability}

trait SeatAvailability[F[_]] {

  def getAvailableSeats(screening: Screening): F[ScreeningSeatAvailability]
}
