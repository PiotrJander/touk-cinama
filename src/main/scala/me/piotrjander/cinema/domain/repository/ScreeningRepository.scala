package me.piotrjander.cinema.domain.repository

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.{Screening, ScreeningId}

trait ScreeningRepository[F[_]] {

  def list(fromTime: LocalDateTime, toTime: LocalDateTime): F[Seq[Screening]]

  def get(id: ScreeningId): F[Option[Screening]]

}
