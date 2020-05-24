package me.piotrjander.cinema.application.validator

import cats.implicits._
import me.piotrjander.cinema.domain.entity.FullName
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FullNameValidatorTest extends AnyFlatSpec with Matchers {

  "FullNameValidator" should "parse correctly" in {
    val validator = new FullNameValidator[Either[Throwable, *]]()
    val result0 = validator.parse("Piątr Jan-Śer")
    assert(result0.toOption.get == FullName("Piątr", "Jan-Śer"))
  }
}
