package me.piotrjander.cinema.infrastructure.database.inmemory

import java.time.LocalDateTime

import cats.Id
import me.piotrjander.cinema.domain.entity.{Screening, ScreeningId}
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningDatabaseView(db: UnderlyingDatabase)
    extends ScreeningRepository[Id] {

  override def list(fromTime: LocalDateTime,
                    toTime: LocalDateTime): Id[Seq[Screening]] =
    db.screenings.values
      .filter(s => s.dateTime.isAfter(fromTime) && s.dateTime.isBefore(toTime))
      .map(s => db.getScreeningEntity(s))
      .toSeq

  override def get(id: ScreeningId): Id[Option[Screening]] =
    db.screenings.get(id).map(s => db.getScreeningEntity(s))

}
