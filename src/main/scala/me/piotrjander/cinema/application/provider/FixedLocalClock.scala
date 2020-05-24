package me.piotrjander.cinema.application.provider

import java.time.LocalDateTime

import cats.effect.Sync
import me.piotrjander.cinema.main.Configuration

class FixedLocalClock[F[_]: Sync] extends LocalClock[F] {

  override def dateTimeNow(): F[LocalDateTime] =
    Sync[F].delay(Configuration.REFERENCE_TIME)
}
