package me.piotrjander.cinema.application.message

import me.piotrjander.cinema.application.EntityPayloads.{Screening, ScreeningSummary}

object ScreeningMessage {

  case class ListRequest(startDateTime: String, endDateTime: String)

  case class ListResponse(screenings: Seq[ScreeningSummary])

  case class DescribeRequest(id: String)

  case class GetResponse(screening: Screening)
}
