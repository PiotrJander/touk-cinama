package me.piotrjander.cinema.application.validator

import cats.MonadError
import me.piotrjander.cinema.application.exception.ValidationException
import me.piotrjander.cinema.domain.entity.TicketsBreakdown

class TicketsBreakdownValidator[F[_]](implicit F: MonadError[F, Throwable]) {

  def validate(input: TicketsBreakdown): F[Unit] = {
    val TicketsBreakdown(adultTickets, studentTickets, childTickets) = input
    val testNonNegative = adultTickets >= 0 && studentTickets >= 0 && childTickets >= 0
    val testExistsPositive = adultTickets > 0 || studentTickets > 0 || childTickets > 0
    F.whenA(!(testNonNegative && testExistsPositive)) {
      F.raiseError(new ValidationException())
    }
  }
}
