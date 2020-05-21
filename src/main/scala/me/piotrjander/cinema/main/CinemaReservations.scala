package me.piotrjander.cinema.main

import java.time.Duration

import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import me.piotrjander.cinema.application.provider.SeatAvailabilityProvider
import me.piotrjander.cinema.application.validator.FullNameValidator
import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{FullName, ScreeningRoom, ScreeningSeatAvailability, Seat, TicketPrices}

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

  def testCheckReservedSeatsAvailable(): Unit = {
    val rowA = Map("1" -> true, "2" -> false)
    val availability = ScreeningSeatAvailability(Map("A" -> rowA))
    val seats = Seq(Seat("A", "2"))
    val result = SeatAvailabilityProvider.checkReservedSeatsAvailable[Either[Throwable, *]](availability, seats)
    assert(result.isRight)
  }

  def testCheckNoEmptySeatBetweenReserved(): Unit = {
    val room = ScreeningRoom(None, "", Map("A" -> Seq("1", "2", "3")))
    val rowA = Map("1" -> false, "2" -> false, "3" -> false)
    val availability = ScreeningSeatAvailability(Map("A" -> rowA))
    val seats = Seq(Seat("A", "1"), Seat("A", "2"))
    val result =
      SeatAvailabilityProvider.checkNoEmptySeatBetweenReserved[Either[Throwable, *]](availability, seats, room)
    assert(result.isRight)
  }

  def main(args: Array[String]): Unit = {
    testCheckNoEmptySeatBetweenReserved()
  }
}
