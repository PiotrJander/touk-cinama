package me.piotrjander.cinema.application

import java.util.regex.Pattern

import cats.MonadError
import me.piotrjander.cinema.domain.entity.FullName

class FullNameValidator[F[_]](implicit F: MonadError[F, Throwable]) {

  def validationException = new ValidationException("Invalid full name")

  def parse(input: String): F[FullName] = {
    val namePartRegex = "\\p{Lu}\\p{Ll}{2,}"
    val lastNameRegex = s"${namePartRegex}|$namePartRegex-$namePartRegex"
    val pattern = Pattern.compile(s"\\s*($namePartRegex)\\s+($lastNameRegex)\\s*")
    val matcher = pattern.matcher(input)
    if (matcher.matches()) {
      F.pure(FullName(matcher.group(1), matcher.group(2)))
    } else {
      F.raiseError(validationException)
    }
  }
}
