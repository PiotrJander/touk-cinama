package me.piotrjander.cinema.infrastructure.database.model

import java.time.LocalDateTime

import me.piotrjander.cinema.domain.entity.{ReservationId, ConfirmationSecret}

case class ReservationRequest(reservation: ReservationId,
                              requestSecret: ConfirmationSecret,
                              submittedTime: LocalDateTime)
