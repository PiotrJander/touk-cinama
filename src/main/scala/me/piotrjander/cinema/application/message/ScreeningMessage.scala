package me.piotrjander.cinema.application.message

import me.piotrjander.cinema.application.EntityPayloads.Screening

object ScreeningMessage {

  case class ListRequest(startDateTime: String, endDateTime: String)

  case class ListResponse(screenings: Seq[Screening])

  case class GetRequest(id: String)

  case class GetResponse(screening: Screening)

}
