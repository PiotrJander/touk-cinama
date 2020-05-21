package me.piotrjander.cinema.main

import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import me.piotrjander.cinema.application.validator.FullNameValidator
import me.piotrjander.cinema.domain.entity.FullName

import scala.collection.immutable.HashMap

object CinemaReservations {

  val RESERVATION_TIMEOUT_MINUTES: Long = 15
  val RESERVATION_BEFORE_START_MINUTES: Long = 15

  def testFullNameValidator(): Unit = {
    val validator = new FullNameValidator[Either[Throwable, *]]()
    val result0 = validator.parse("Piątr Jan-Śer")
    assert(result0.toOption.get == FullName("Piątr", "Jan-Śer"))
  }

  def testCirce(): Unit = {
    var seats = new HashMap[String, Seq[String]]()
    seats += ("foo" -> Seq("bar"))
    println(seats.asJson.toString())
  }

  def main(args: Array[String]): Unit = {
    testCirce()
  }
}
