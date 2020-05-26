package me.piotrjander.cinema.domain.entity

case class TicketPrices(adult: Float, student: Float, child: Float) {

  val amountToPay: TicketsBreakdown => Float = {
    case TicketsBreakdown(adultTickets, studentTickets, childTickets) =>
      adult * adultTickets + student * studentTickets + child * childTickets
  }
}
