package me.piotrjander.cinema.application.provider

import java.time.LocalDateTime

import cats.MonadError
import cats.effect.Sync
import cats.implicits._
import me.piotrjander.cinema.application.exception.BadRequestException
import me.piotrjander.cinema.domain.entity._
import me.piotrjander.cinema.domain.repository._

import scala.collection.mutable

class SeatAvailabilityProvider[F[_]: Sync](
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F]
) extends SeatAvailability[F] {

  import SeatAvailabilityProvider._

  private def unconfirmedValidReservations(
    reservations: Seq[Reservation],
    requests: Seq[Option[ReservationRequest]],
    dateTimeNow: LocalDateTime
  ): Seq[Reservation] =
    (reservations zip requests).foldRight(Vector.empty[Reservation]) {
      case ((reservation, Some(request)), acc) if !request.isExpired(dateTimeNow) =>
        reservation +: acc
      case (_, acc) =>
        acc
    }

  override def getAvailableSeats(
    screening: Screening
  ): F[ScreeningSeatAvailability] = {
    for {
      reservations <- reservationRepository.list(screening.id.get)
      confirmedReservations = reservations.filter(_.confirmed)
      unconfirmedReservations = reservations.filter(!_.confirmed).toVector
      reservationRequests <- unconfirmedReservations.traverse(
        r => reservationRequestRepository.get(r.id.get)
      )
      dateTimeNow <- localClock.dateTimeNow()
      validReservations = confirmedReservations ++
        unconfirmedValidReservations(
          unconfirmedReservations,
          reservationRequests,
          dateTimeNow
        )
    } yield calculateAvailableSeats(screening, validReservations)
  }

  override def validateSeatSelection(screening: Screening,
                                     seats: Seq[Seat]): F[Unit] =
    for {
      availability <- getAvailableSeats(screening)
      _ <- checkReservedSeatsAvailable[F](availability, seats)
      _ <- checkNoEmptySeatBetweenReserved[F](availability, seats, screening.room)
    } yield ()
}

object SeatAvailabilityProvider {

  def checkReservedSeatsAvailable[F[_]](
    availability: ScreeningSeatAvailability,
    seats: Seq[Seat]
  )(implicit F: MonadError[F, Throwable]): F[Unit] = {
    for (seat <- seats) {
      if (availability.isReserved(seat)) {
        return F.raiseError(new BadRequestException("The seat is already taken"))
      }
    }
    F.unit
  }

  def checkNoEmptySeatBetweenReserved[F[_]](
    availability: ScreeningSeatAvailability,
    seats: Seq[Seat],
    room: ScreeningRoom
  )(implicit F: MonadError[F, Throwable]): F[Unit] = {
    for ((row, seats) <- seats.groupBy(_.row)) {
      val reservedSeats = seats.map(_.name).toSet
      val rowSeats = room.seats(row)
      if (rowSeats.length >= 2) {
        for (i <- 2 until rowSeats.length) {
          val reserved0 = reservedSeats.contains(rowSeats(i))
          val reserved1 = reservedSeats.contains(rowSeats(i - 1))
          val reserved2 = reservedSeats.contains(rowSeats(i - 2))
          val empty1 = !reserved1 && !availability.isReserved(row, rowSeats(i - 1))
          if (reserved0 && reserved2 && empty1) {
            return F.raiseError(new BadRequestException("Empty seat between reserved seats"))
          }
        }
      }
    }
    F.unit
  }

  private def calculateAvailableSeats(
    screening: Screening,
    reservations: Seq[Reservation]
  ): ScreeningSeatAvailability = {
    val seats = reservations.flatMap(_.seats)
    val seatMap = mutable.Map.empty[String, mutable.Map[String, Boolean]]

    for (row <- screening.room.seats.keys) {
      seatMap(row) = mutable.Map.empty[String, Boolean]
      for (seat <- screening.room.seats(row)) {
        seatMap(row)(seat) = false
      }
    }

    for (Seat(row, name) <- seats) {
      seatMap(row)(name) = true
    }

    var immutableSeatMap = Map.empty[String, Map[String, Boolean]]
    for ((row, seats) <- seatMap) {
      immutableSeatMap += (row -> seats.toMap)
    }

    ScreeningSeatAvailability(immutableSeatMap)
  }
}
