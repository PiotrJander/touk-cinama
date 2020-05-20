package me.piotrjander.cinema.application

import java.time.LocalDateTime

trait LocalClock[F[_]] {

  def dateTimeNow(): F[LocalDateTime]
}
