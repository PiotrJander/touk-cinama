package me.piotrjander.cinema.application.provider

import cats.Applicative
import me.piotrjander.cinema.domain.entity.ConfirmationSecret

/** We use a constant secret generator to have deterministic tests. */
class FixedConfirmationSecretGenerator[F[_]: Applicative] extends ConfirmationSecretGenerator[F] {

  override def generate: F[ConfirmationSecret] = Applicative[F].pure {
    ConfirmationSecret("secretkey")
  }
}
