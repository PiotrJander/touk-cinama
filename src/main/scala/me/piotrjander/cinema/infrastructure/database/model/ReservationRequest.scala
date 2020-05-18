package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

case class ReservationRequest(reservation: ReservationId,
                              requestSecret: String,
                              submittedTime: LocalDateTime)
