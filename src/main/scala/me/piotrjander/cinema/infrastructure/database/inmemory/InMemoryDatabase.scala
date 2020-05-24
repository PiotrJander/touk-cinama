package me.piotrjander.cinema.infrastructure.database.inmemory

import me.piotrjander.cinema.domain.entity
import me.piotrjander.cinema.domain.entity.{Movie, MovieId, ScreeningRoom}
import me.piotrjander.cinema.infrastructure.database.model

/** TODO make concurrent or just comment */
class InMemoryDatabase {

  // Arbitrary initial number which doesn't collide with hand-picked IDs during populating
  // the database with fixtures.
  var nextId: Int = 10

  var movies: Map[entity.MovieId, entity.Movie] = Map.empty
  var screeningRooms: Map[entity.ScreeningRoomId, entity.ScreeningRoom] = Map.empty
  var screenings: Map[entity.ScreeningId, model.Screening] = Map.empty
  var reservations: Map[entity.ReservationId, model.Reservation] = Map.empty
  var reservationRequests: Map[entity.ReservationId, model.ReservationRequest] = Map.empty

  def addMovie(movie: Movie): Unit = {
    movies += movie.id.get -> movie
  }

  def addScreeningRoom(room: ScreeningRoom): Unit = {
    screeningRooms += room.id.get -> room
  }

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

  def getReservationRequestEntity(reservationRequestModel: model.ReservationRequest): entity.ReservationRequest = {
    val model.ReservationRequest(reservationId, confirmationSecret, submittedTime) = reservationRequestModel
    val reservation = getReservationEntity(reservations(reservationId))
    entity.ReservationRequest(reservation, confirmationSecret, submittedTime)
  }
}
