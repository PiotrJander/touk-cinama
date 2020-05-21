package me.piotrjander.cinema.domain.entity

case class TicketsBreakdown(adultTickets: Int,
                            studentTickets: Int,
                            childTickets: Int) {

  def numberOfTickets: Int = adultTickets + studentTickets + childTickets
}
