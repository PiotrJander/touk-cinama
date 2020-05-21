package me.piotrjander.cinema.main

import java.time.Duration

import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import me.piotrjander.cinema.application.validator.FullNameValidator
import me.piotrjander.cinema.domain.entity.{FullName, TicketPrices}

import scala.collection.immutable.HashMap

object CinemaReservations {

  val RESERVATION_TIMEOUT: Duration = Duration.ofMinutes(15)
  val RESERVATION_BEFORE_START: Duration = Duration.ofMinutes(15)
  val TICKET_PRICES: TicketPrices = TicketPrices(25, 18, 12.50f)

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
