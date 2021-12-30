package info.galudisu.schedulecompute

import zio._

object ZLayers {

  case class User(name: String, email: String)

  object UserEmailer {
    trait Service {
      def notify(user: User, message: String): Task[Unit]
    }

    val live: ZLayer[Any, Nothing, Has[UserEmailer.Service]] =
      ZLayer.succeed((user: User, message: String) =>
        Task {
          println(s"[User emailer] Sending '$message' to ${user.email}'")
      })

    def notify(user: User, message: String): ZIO[Has[UserEmailer.Service], Throwable, Unit] =
      ZIO.accessM(hasService => hasService.get.notify(user, message))
  }

}
