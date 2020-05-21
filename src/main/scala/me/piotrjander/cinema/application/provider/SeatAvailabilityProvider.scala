package me.piotrjander.cinema.application.provider

import java.time.LocalDateTime

import cats.effect.Sync
import cats.implicits._
import me.piotrjander.cinema.domain.entity._
import me.piotrjander.cinema.domain.repository._

import scala.collection.mutable

class SeatAvailabilityProvider[F[_]: Sync](
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F],
  reservationRequestExpirationChecker: ReservationRequestExpirationChecker
) extends SeatAvailability[F] {

  import SeatAvailabilityProvider._

  private def unconfirmedValidReservations(reservations: Seq[Reservation], requests: Seq[Option[ReservationRequest]], dateTimeNow: LocalDateTime): Seq[Reservation] =
    (reservations zip requests).foldRight(Vector.empty) {
      case ((reservation, Some(request)), acc)
        if reservationRequestExpirationChecker.isExpired(request, dateTimeNow) =>
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
      reservationRequests <- unconfirmedReservations.traverse(r => reservationRequestRepository.get(r.id.get))
      dateTimeNow <- localClock.dateTimeNow()
      validReservations =
        confirmedReservations ++
          unconfirmedValidReservations(unconfirmedReservations, reservationRequests, dateTimeNow)
    } yield calculateAvailableSeats(screening, validReservations)
  }
}

object SeatAvailabilityProvider {

  private def calculateAvailableSeats(screening: Screening, reservations: Seq[Reservation]): ScreeningSeatAvailability = {
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
