package me.piotrjander.cinema.application.provider

import cats.Applicative
import me.piotrjander.cinema.domain.entity.ConfirmationSecret

class FixedConfirmationSecretGenerator[F[_]: Applicative] extends ConfirmationSecretGenerator[F] {

  override def generate: F[ConfirmationSecret] = Applicative[F].pure {
    ConfirmationSecret("secretkey")
  }
}
