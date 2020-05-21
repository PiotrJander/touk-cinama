package me.piotrjander.cinema.domain.entity

case class ConfirmationSecret(secret: String) extends AnyVal {
  override def toString: String = secret
}
