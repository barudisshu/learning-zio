package info.galudisu.netty
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import info.galudisu.netty.Dependencies.ExtServices
import zio.ULayer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main extends App {

  implicit val layer: ULayer[ExtServices]                 = Dependencies.extServicesLive
  implicit val system: ActorSystem[Nothing]               = ActorSystem(Behaviors.empty, "JobApp")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val akkaHttpService = new AkkaHttpService()
  val bindingFuture   = Http().newServerAt("127.0.0.1", 8000).bind(akkaHttpService.routes)

  StdIn.readLine("Hit ENTER to exit")

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
