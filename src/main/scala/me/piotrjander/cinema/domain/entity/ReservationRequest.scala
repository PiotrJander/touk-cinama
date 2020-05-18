package me.piotrjander.cinema.domain.entity

import java.time.LocalDateTime

case class ReservationRequest(reservation: Reservation,
                              requestSecret: ReservationRequestSecret,
                              submittedTime: LocalDateTime)
