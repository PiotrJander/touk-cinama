package me.piotrjander.cinema.application

class ValidationException(msg: String) extends BadRequestException(msg) {

  def this() {
    this("Validation error")
  }
}
