package me.piotrjander.cinema.domain.entity

import java.time.LocalDateTime

import me.piotrjander.cinema.main.Configuration

case class ReservationRequest(reservation: Reservation,
                              confirmationSecret: ConfirmationSecret,
                              submittedDateTime: LocalDateTime) {

  def isExpired(dateTimeNow: LocalDateTime): Boolean = {
    val movieStartDateTime = reservation.screening.dateTime
    submittedDateTime.plus(Configuration.RESERVATION_TIMEOUT).isAfter(dateTimeNow) &&
      dateTimeNow.isBefore(movieStartDateTime.minus(Configuration.RESERVATION_BEFORE_START))
  }
}
