package me.piotrjander.cinema.domain.repository

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.Screening

trait ScreeningRepository[F[_]] {

  def list(fromTime: LocalDateTime, toTime: LocalDateTime): F[Seq[Screening]]

  def get(id: String): F[Option[Screening]]

}
