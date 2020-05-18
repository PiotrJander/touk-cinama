package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.infrastructure.database.model

/** TODO make concurrent or just comment */
class UnderlyingDatabase {

  var nextId: Int = 0

  var movies: Map[entity.MovieId, entity.Movie] = Map.empty
  var screeningRooms: Map[entity.ScreeningRoomId, entity.ScreeningRoom] = Map.empty
  var screenings: Map[entity.ScreeningId, model.Screening] = Map.empty
  var reservations: Map[entity.ReservationId, model.Reservation] = Map.empty
  var reservationRequests: Map[entity.ReservationId, model.ReservationRequest] = Map.empty

  def getNextId: String = {
    nextId += 1
    nextId.toString
  }

  def getScreeningEntity(screeningModel: model.Screening): entity.Screening = {
    val model.Screening(id, movieId, roomId, dateTime) = screeningModel
    entity.Screening(Some(id), movies(movieId), screeningRooms(roomId), dateTime)
  }

  def getReservationEntity(reservationModel: model.Reservation): entity.Reservation = {
    val model.Reservation(id, screeningId, name, ticketsBreakdown, seats, confirmed) = reservationModel
    val screening = getScreeningEntity(screenings(screeningId))
    entity.Reservation(Some(id), screening, name, ticketsBreakdown, seats, confirmed)
  }
}
