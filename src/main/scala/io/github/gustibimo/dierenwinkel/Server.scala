package io.github.gustibimo.dierenwinkel

import cats.effect._
import cats.implicits._
import fs2.Stream
import io.github.gustibimo.dierenwinkel.config.{DatabaseConfig, PetStoreConfig}
import io.github.gustibimo.dierenwinkel.endpoint.{OrderEndpoints, PetEndpoints}
import io.github.gustibimo.dierenwinkel.repository.{
  DoobieOrderRepositoryInterpreter,
  DoobiePetRepositoryInterpreter
}
import io.github.gustibimo.dierenwinkel.service.{OrderService, PetService}
import io.github.gustibimo.dierenwinkel.validation.PetValidationInterpreter
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.StreamApp
import org.http4s.util.ExitCode

object Server extends StreamApp[IO] {

  override def stream(args: List[String], shutdown: IO[Unit]): Stream[IO, ExitCode] =
    createStream[IO](args, shutdown).unsafeRunSync()

  def createStream[F[_]](args: List[String], shutdown: F[Unit])(
      implicit E: Effect[F]): F[Stream[F, ExitCode]] =
    for {
      conf <- PetStoreConfig.load[F]
      xa <- DatabaseConfig.dbTransactor(conf.db)
      _ <- DatabaseConfig.initializeDb(conf.db, xa)
      petRepo = DoobiePetRepositoryInterpreter[F](xa)
      orderRepo = DoobieOrderRepositoryInterpreter[F](xa)
    } yield {

      val petValidation = PetValidationInterpreter[F](petRepo)
      val petService = PetService[F](petRepo, petValidation)
      val orderService = OrderService[F](orderRepo)

      BlazeBuilder[F]
        .bindHttp(8088, "localhost")
        .mountService(PetEndpoints.endpoints[F](petService), "/")
        .mountService(OrderEndpoints.endpoints[F](orderService), "/")
        .serve
    }
}
