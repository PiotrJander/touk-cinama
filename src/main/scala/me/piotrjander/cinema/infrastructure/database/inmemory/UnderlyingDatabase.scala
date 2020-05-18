package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.infrastructure.database.model

/** TODO make concurrent or just comment */
class UnderlyingDatabase {

  var movies: Map[model.MovieId, model.Movie] = Map.empty
  var screeningRooms: Map[model.ScreeningRoomId, model.ScreeningRoom] = Map.empty
  var screenings: Map[model.ScreeningId, model.Screening] = Map.empty
  var reservations: Map[model.ReservationId, model.Reservation] = Map.empty
  var reservationRequests: Map[model.ReservationId, model.ReservationRequest] = Map.empty

  def getScreeningEntity(screeningModel: model.Screening): entity.Screening = {
    val model.Screening(id, movieId, roomId, dateTime) = screeningModel
    entity.Screening(id.id, movies(movieId).toEntity, screeningRooms(roomId).toEntity, dateTime)
  }
}
