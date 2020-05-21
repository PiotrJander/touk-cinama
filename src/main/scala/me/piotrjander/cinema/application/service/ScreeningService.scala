package me.piotrjander.cinema.application.service

import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.validator.DateTimeValidator
import me.piotrjander.cinema.application.{EntityPayloads, LocalClock}
import me.piotrjander.cinema.domain.entity.ScreeningId
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningService[F[_]: Async](screeningRepository: ScreeningRepository[F],
                                    localClock: LocalClock[F])
    extends ScreeningServiceApi[F] {

  val F: Async[F] = implicitly[Async[F]]

  val dateTimeValidator = new DateTimeValidator[F]()

  override def list(request: ListRequest): F[ListResponse] =
    for {
      startDateTime <- dateTimeValidator.parse(request.startDateTime)
      endDateTime <- dateTimeValidator.parse(request.endDateTime)
      dateTimeNow <- localClock.dateTimeNow()
      // TODO focus on happy path now, later handle past dates
      screenings <- screeningRepository.list(startDateTime, endDateTime)
    } yield {
      val screeningPayloads =
        screenings.map(s => EntityPayloads.Screening.fromEntity(s))
      ListResponse(screeningPayloads)
    }

  override def get(screeningId: GetRequest): F[Option[GetResponse]] =
    for {
      maybeScreening <- screeningRepository.get(ScreeningId(screeningId.id))
    } yield {
      maybeScreening.map { s =>
        val screening = EntityPayloads.Screening.fromEntity(s)
        GetResponse(screening)
      }
    }
}
