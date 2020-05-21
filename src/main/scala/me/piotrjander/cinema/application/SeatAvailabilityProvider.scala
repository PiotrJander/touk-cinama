package me.piotrjander.cinema.application

import java.time.LocalDateTime

import cats.effect.Sync
import cats.implicits._
import me.piotrjander.cinema.domain.entity._
import me.piotrjander.cinema.domain.repository._
import me.piotrjander.cinema.main.CinemaReservations

class SeatAvailabilityProvider[F[_]: Sync](
  reservationRepository: ReservationRepository[F],
  reservationRequestRepository: ReservationRequestRepository[F],
  localClock: LocalClock[F],
  reservationRequestExpirationChecker: ReservationRequestExpirationChecker
) extends SeatAvailability[F] {

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
    } yield ???
  }
}
