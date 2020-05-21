package me.piotrjander.cinema.application

import java.time.{Duration, LocalDateTime}

import me.piotrjander.cinema.domain.entity.ReservationRequest

class ReservationRequestExpirationChecker(reservationTimeout: Duration,
                                          reservationBeforeStart: Duration) {

  def isExpired(reservationRequest: ReservationRequest,
                dateTimeNow: LocalDateTime): Boolean = {
    val movieStartDateTime = reservationRequest.reservation.screening.dateTime
    val submittedDateTime = reservationRequest.submittedTime
    submittedDateTime.plus(reservationTimeout).isAfter(dateTimeNow) &&
    dateTimeNow.isBefore(movieStartDateTime.minus(reservationBeforeStart))
  }
}
