package me.piotrjander.cinema.application.validator

import java.time.LocalDateTime
import java.time.format.DateTimeParseException

import cats.MonadError
import me.piotrjander.cinema.application.exception.ValidationException

class DateTimeValidator[F[_]](implicit F: MonadError[F, Throwable]) {

  def parse(input: String): F[LocalDateTime] = {
    try F.pure(LocalDateTime.parse(input))
    catch {
      case _: DateTimeParseException =>
        F.raiseError(new ValidationException())
    }
  }
}
