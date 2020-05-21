package me.piotrjander.cinema.application

class BadRequestException(msg: String) extends Exception(msg) {

  def this() = this("We don't know anything else")
}
