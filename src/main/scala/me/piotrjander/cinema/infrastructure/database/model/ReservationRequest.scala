package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

case class ReservationRequest(reservationId: String,
                              requestSecret: String,
                              submittedTime: LocalDateTime)
