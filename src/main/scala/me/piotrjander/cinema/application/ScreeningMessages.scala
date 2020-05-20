package me.piotrjander.cinema.application

import EntityPayloads.Screening

object ScreeningMessages {

  case class ListRequest(startDateTime: String, endDateTime: String)

  case class ListResponse(screenings: Seq[Screening])

  case class GetRequest(id: String)

  case class GetResponse(screening: Screening)
}
