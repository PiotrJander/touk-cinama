package me.piotrjander.cinema.application.exception

class ValidationException(msg: String) extends BadRequestException(msg) {

  def this() {
    this("Validation error")
  }
}
