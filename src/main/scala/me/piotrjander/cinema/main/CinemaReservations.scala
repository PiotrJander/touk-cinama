package me.piotrjander.cinema.main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import cats.effect.{ContextShift, IO}
import me.piotrjander.cinema.application.provider.{
  ConfirmationSecretUUIDGenerator,
  DefaultLocalClock,
  SeatAvailabilityProvider
}
import me.piotrjander.cinema.application.service.{
  ReservationService,
  ScreeningService
}
import me.piotrjander.cinema.infrastructure.database.inmemory.{
  ReservationDatabaseView,
  ReservationRequestDatabaseView,
  ScreeningDatabaseView,
  InMemoryDatabase
}
import me.piotrjander.cinema.infrastructure.http.{
  ReservationController,
  Routes,
  ScreeningController
}

import scala.util.{Failure, Success}

object CinemaReservations extends App {

  implicit val system: ActorSystem = ActorSystem("main")
  import system.dispatcher
  implicit val cs: ContextShift[IO] = IO.contextShift(system.dispatcher)

  val database = new InMemoryDatabase()

  val screeningRepository = new ScreeningDatabaseView[IO](database)
  val reservationRepository = new ReservationDatabaseView[IO](database)
  val reservationRequestRepository =
    new ReservationRequestDatabaseView[IO](database)

  val localClock = new DefaultLocalClock[IO]()
  val seatAvailabilityProvider =
    new SeatAvailabilityProvider[IO](
      reservationRepository,
      reservationRequestRepository,
      localClock
    )
  val secretGenerator = new ConfirmationSecretUUIDGenerator[IO]()

  val screeningService = new ScreeningService[IO](
    screeningRepository,
    localClock,
    seatAvailabilityProvider
  )
  val reservationService = new ReservationService[IO](
    screeningRepository,
    reservationRepository,
    reservationRequestRepository,
    localClock,
    secretGenerator,
    seatAvailabilityProvider
  )

  val screeningController = new ScreeningController(screeningService)
  val reservationController = new ReservationController(reservationService)

  val routes = new Routes(screeningController, reservationController)

  val serverBinding =
    Http().bindAndHandle(routes.routes, Configuration.HOST, Configuration.PORT)

  serverBinding.onComplete {
    case Success(_) =>
      system.log.info("Listening...")
    case Failure(exception) =>
      // A failure would most likely be due to the port being in use.
      system.log.error("Server could not start!", exception)
      system.terminate()
  }
}
