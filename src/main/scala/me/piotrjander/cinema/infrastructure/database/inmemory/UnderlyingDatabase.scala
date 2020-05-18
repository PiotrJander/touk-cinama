package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.infrastructure.database.model

/** TODO make concurrent or just comment */
class UnderlyingDatabase {

  var movies: Map[String, model.Movie] = Map.empty
  var screeningRooms: Map[String, model.ScreeningRoom] = Map.empty
  var screenings: Map[String, model.Screening] = Map.empty
  var reservations: Map[String, model.Reservation] = Map.empty
  var reservationRequests: Map[String, model.ReservationRequest] = Map.empty

  def getScreeningEntity(screeningModel: model.Screening): entity.Screening = {
    val model.Screening(id, movieId, roomId, dateTime) = screeningModel
    entity.Screening(id, movies(movieId).toEntity, screeningRooms(roomId).toEntity, dateTime)
  }
}
