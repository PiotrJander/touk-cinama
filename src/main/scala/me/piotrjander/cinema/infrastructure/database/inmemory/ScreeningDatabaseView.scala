package me.piotrjander.cinema.infrastructure.database.inmemory

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.{Screening, ScreeningId}
import me.piotrjander.cinema.domain.repository.ScreeningRepository

import scala.util.Try

class ScreeningDatabaseView(db: UnderlyingDatabase)
    extends ScreeningRepository[Try] {

  override def list(fromTime: LocalDateTime,
                    toTime: LocalDateTime): Try[Seq[Screening]] = Try {
    db.screenings.values
      .filter(s => s.dateTime.isAfter(fromTime) && s.dateTime.isBefore(toTime))
      .map(s => db.getScreeningEntity(s))
      .toSeq
  }

  override def get(id: ScreeningId): Try[Option[Screening]] = Try {
    db.screenings.get(id).map(s => db.getScreeningEntity(s))
  }
}
