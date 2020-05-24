package me.piotrjander.cinema.domain.entity

case class TicketsBreakdown(adults: Int,
                            students: Int,
                            children: Int) {

  def numberOfTickets: Int = adults + students + children
}
