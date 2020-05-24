package me.piotrjander.cinema.application.service

import java.time.LocalDateTime

import cats.Applicative
import cats.data.OptionT
import cats.effect.Async
import cats.implicits._
import me.piotrjander.cinema.application.EntityPayloads
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.application.message.ScreeningMessage._
import me.piotrjander.cinema.application.provider.{LocalClock, SeatAvailability}
import me.piotrjander.cinema.application.validator.DateTimeValidator
import me.piotrjander.cinema.domain.entity.ScreeningId
import me.piotrjander.cinema.domain.repository.ScreeningRepository
import me.piotrjander.cinema.main.Configuration

class ScreeningService[F[_]: Async](screeningRepository: ScreeningRepository[F],
                                    localClock: LocalClock[F],
                                    seatAvailability: SeatAvailability[F])
    extends ScreeningServiceApi[F] {

  val dateTimeValidator = new DateTimeValidator[F]()

  private def parseListRequest(request: ListRequest): F[(LocalDateTime, LocalDateTime)] =
    for {
      startDateTime <- dateTimeValidator.parse(request.startDateTime)
      endDateTime <- dateTimeValidator.parse(request.endDateTime)
      dateTimeNow <- localClock.dateTimeNow()
      cutoffDateTime = dateTimeNow.plus(Configuration.RESERVATION_BEFORE_START)
      _ <- Applicative[F].whenA(endDateTime.isBefore(cutoffDateTime)) {
        Async[F].raiseError(new BadRequestException("Invalid time range"))
      }
    } yield {
      val start = Seq(startDateTime, cutoffDateTime).max
      (start, endDateTime)
    }

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

  override def describe(screeningId: DescribeRequest): F[Option[GetResponse]] = {
    val result = for {
      screening <- OptionT(screeningRepository.get(ScreeningId(screeningId.id)))
      seatAvailability <- OptionT.liftF(seatAvailability.getAvailableSeats(screening))
    } yield {
      val screeningPayload =
        EntityPayloads.Screening.fromEntity(screening, seatAvailability)
      GetResponse(screeningPayload)
    }
    result.value
  }
}
