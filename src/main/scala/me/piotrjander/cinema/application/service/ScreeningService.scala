package me.piotrjander.cinema.application.service

import cats.effect.Async
import cats.implicits._
import cats.data.OptionT
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.validator.DateTimeValidator
import me.piotrjander.cinema.application.{
  EntityPayloads,
  LocalClock,
  SeatAvailability
}
import me.piotrjander.cinema.domain.entity.ScreeningId
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningService[F[_]: Async](screeningRepository: ScreeningRepository[F],
                                    localClock: LocalClock[F],
                                    seatAvailability: SeatAvailability[F])
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
        screenings.map(s => EntityPayloads.ScreeningSummary.fromEntity(s))
      ListResponse(screeningPayloads)
    }

  override def get(screeningId: GetRequest): F[Option[GetResponse]] = {
    val result = for {
      screening <- OptionT(screeningRepository.get(ScreeningId(screeningId.id)))
      seatAvailability <- OptionT.liftF(seatAvailability.getAvailableSeats(screening))
    } yield {
      val screening = EntityPayloads.Screening.fromEntity(screening, seatAvailability)
      GetResponse(screening)
    }
    result.value
  }
}
