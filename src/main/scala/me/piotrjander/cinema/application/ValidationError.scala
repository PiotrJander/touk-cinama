package me.piotrjander.cinema.application

class ValidationError(msg: String) extends Exception(msg) {

  def this() {
    this("Validation error")
  }
}
