package me.piotrjander.cinema.application.provider

import java.time.LocalDateTime

trait LocalClock[F[_]] {

  def dateTimeNow(): F[LocalDateTime]
}
