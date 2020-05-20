package me.piotrjander.cinema.application

import java.time.LocalDateTime
import java.time.format.DateTimeParseException

import cats.MonadError

class DateTimeValidator[F[_]](implicit F: MonadError[F, Throwable]) {

  def parse(dateTime: String): F[LocalDateTime] = {
    try F.pure(LocalDateTime.parse(dateTime))
    catch {
      case _: DateTimeParseException =>
        F.raiseError(new ValidationError())
    }
  }
}
