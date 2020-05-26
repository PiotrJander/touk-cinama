package me.piotrjander.cinema.infrastructure.database.inmemory

import java.time.LocalDateTime

import cats.effect.Sync
import me.piotrjander.cinema.domain.entity.{Screening, ScreeningId}
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningDatabaseView[F[_]: Sync](database: InMemoryDatabase)
    extends ScreeningRepository[F] {

  val F: Sync[F] = implicitly[Sync[F]]

  override def list(fromTime: LocalDateTime, toTime: LocalDateTime): F[Seq[Screening]] =
    F.delay {
      database.screenings.values
        .filter(_.dateTime.isAfter(fromTime))
        .filter(_.dateTime.isBefore(toTime))
        .map(s => database.getScreeningEntity(s))
        .toSeq
    }

  override def get(id: ScreeningId): F[Option[Screening]] = F.delay {
    database.screenings.get(id).map(s => database.getScreeningEntity(s))
  }
}
