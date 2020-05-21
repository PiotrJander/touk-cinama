package me.piotrjander.cinema.application.validator

import cats.MonadError
import me.piotrjander.cinema.application.exception.ValidationException
import me.piotrjander.cinema.domain.entity.{Seat, TicketsBreakdown}

class TicketsBreakdownValidator[F[_]](implicit F: MonadError[F, Throwable]) {

  def validate(ticketsBreakdown: TicketsBreakdown, seats: Seq[Seat]): F[Unit] = {
    val TicketsBreakdown(adultTickets, studentTickets, childTickets) = ticketsBreakdown
    val testNonNegative = adultTickets >= 0 && studentTickets >= 0 && childTickets >= 0
    val testExistsPositive = adultTickets > 0 || studentTickets > 0 || childTickets > 0
    val correctNumberOfSeats = ticketsBreakdown.numberOfTickets == seats.length
    F.whenA(!(testNonNegative && testExistsPositive && correctNumberOfSeats)) {
      F.raiseError(new ValidationException())
    }
  }
}
