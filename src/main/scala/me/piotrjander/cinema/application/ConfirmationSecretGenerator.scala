package me.piotrjander.cinema.application

import me.piotrjander.cinema.domain.entity.ConfirmationSecret

trait ConfirmationSecretGenerator[F[_]] {

  def generate: F[ConfirmationSecret]
}
