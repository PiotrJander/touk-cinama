package me.piotrjander.cinema.main

import cats.implicits._
import me.piotrjander.cinema.application.validator.FullNameValidator
import me.piotrjander.cinema.domain.entity.FullName

object CinemaReservations {

  val RESERVATION_TIMEOUT_MINUTES: Long = 15
  val RESERVATION_BEFORE_START_MINUTES: Long = 15

  def main(args: Array[String]): Unit = {
    val validator = new FullNameValidator[Either[Throwable, *]]()
    val result0 = validator.parse("Piątr Jan-Śer")
    assert(result0.toOption.get == FullName("Piątr", "Jan-Śer"))
  }
}
