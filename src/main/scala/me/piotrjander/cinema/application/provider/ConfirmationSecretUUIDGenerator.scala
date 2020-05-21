package me.piotrjander.cinema.application.provider

import java.util.UUID

import cats.Applicative
import me.piotrjander.cinema.domain.entity.ConfirmationSecret

class ConfirmationSecretUUIDGenerator[F[_]: Applicative] extends ConfirmationSecretGenerator[F] {

  override def generate: F[ConfirmationSecret] = Applicative[F].pure {
    ConfirmationSecret(UUID.randomUUID().toString)
  }
}