package me.piotrjander.cinema.application

import cats.effect.Async
import me.piotrjander.cinema.domain.repository.ScreeningRepository

class ScreeningService[F[_]: Async](screeningRepository: ScreeningRepository[F])
    extends ScreeningServiceApi[F] {

  override def list(request: ScreeningMessages.ListRequest): F[ScreeningMessages.ListResponse] = ???

  override def get(screeningId: ScreeningMessages.GetRequest): F[Option[ScreeningMessages.GetResponse]] = ???
}
