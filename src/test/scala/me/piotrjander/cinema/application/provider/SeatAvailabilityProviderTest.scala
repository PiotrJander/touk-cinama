package me.piotrjander.cinema.application.provider

import cats.implicits._
import me.piotrjander.cinema.domain.entity.{ScreeningRoom, ScreeningSeatAvailability, Seat}
import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec

class SeatAvailabilityProviderTest extends AnyFlatSpec with Matchers with EitherValues {

  "SeatAvailabilityProvider" should "check that reserved seats are available" in {
    val rowA = Map("1" -> true, "2" -> false)
    val availability = ScreeningSeatAvailability(Map("A" -> rowA))
    val seats = Seq(Seat("A", "2"))
    val result = SeatAvailabilityProvider.checkReservedSeatsAvailable[Either[Throwable, *]](availability, seats)
    assert(result.isRight)
  }

  it should "check that there are no single empty seat between reserved seats" in {
    val room = ScreeningRoom(None, "", Map("A" -> Seq("1", "2", "3")))
    val rowA = Map("1" -> false, "2" -> false, "3" -> false)
    val availability = ScreeningSeatAvailability(Map("A" -> rowA))
    val seats = Seq(Seat("A", "1"), Seat("A", "2"))
    val result =
      SeatAvailabilityProvider.checkNoEmptySeatBetweenReserved[Either[Throwable, *]](availability, seats, room)
    assert(result.isRight)
  }
}
