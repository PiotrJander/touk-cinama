package me.piotrjander.cinema.domain.entity

case class ConfirmationSecret(secret: String) extends AnyVal {
  override def toString: String = secret

  def equalsString(s: String): Boolean = secret.equals(s)
}
