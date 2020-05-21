package me.piotrjander.cinema.application.service

import java.time.{Duration, LocalDateTime}

import cats.Applicative
import cats.effect.Async
import cats.implicits._
import cats.data.OptionT
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.validator.DateTimeValidator
import me.piotrjander.cinema.application.EntityPayloads
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.provider.{LocalClock, SeatAvailability}
import me.piotrjander.cinema.domain.entity.ScreeningId
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningService[F[_]: Async](screeningRepository: ScreeningRepository[F],
                                    localClock: LocalClock[F],
                                    seatAvailability: SeatAvailability[F],
                                    beforeMovieStart: Duration)
    extends ScreeningServiceApi[F] {

  val dateTimeValidator = new DateTimeValidator[F]()

  private def parseListRequest(request: ListRequest): F[(LocalDateTime, LocalDateTime)] =
    for {
      startDateTime <- dateTimeValidator.parse(request.startDateTime)
      endDateTime <- dateTimeValidator.parse(request.endDateTime)
      dateTimeNow <- localClock.dateTimeNow()
      cutoffDateTime = dateTimeNow.plus(beforeMovieStart)
      _ <- Applicative[F].whenA(endDateTime.isBefore(cutoffDateTime)) {
        Async[F].raiseError(new BadRequestException())
      }
    } yield (Seq(startDateTime, cutoffDateTime).max, endDateTime)

  override def list(request: ListRequest): F[ListResponse] =
    for {
      parameters <- parseListRequest(request)
      screenings <- (screeningRepository.list _).tupled(parameters)
    } yield {
      val screeningPayloads =
        screenings
          .map(s => EntityPayloads.ScreeningSummary.fromEntity(s))
          .sorted
      ListResponse(screeningPayloads)
    }

  override def get(screeningId: GetRequest): F[Option[GetResponse]] = {
    val result = for {
      screening <- OptionT(screeningRepository.get(ScreeningId(screeningId.id)))
      seatAvailability <- OptionT.liftF(seatAvailability.getAvailableSeats(screening))
    } yield {
      val screening =
        EntityPayloads.Screening.fromEntity(screening, seatAvailability)
      GetResponse(screening)
    }
    result.value
  }
}
