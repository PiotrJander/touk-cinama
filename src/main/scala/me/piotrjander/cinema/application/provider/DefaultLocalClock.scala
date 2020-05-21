package me.piotrjander.cinema.application.provider

import java.time.LocalDateTime

import cats.effect.Sync

class DefaultLocalClock[F[_]: Sync] extends LocalClock[F] {

  override def dateTimeNow(): F[LocalDateTime] =
    Sync[F].delay(LocalDateTime.now())
}
