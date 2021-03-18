package info.galudisu

import akka.http.scaladsl.server.Route
import doobie.util.transactor.Transactor.Aux
import zio._

package object netty {
  type IOTransactor = Aux[Task, Unit]

  implicit class ZioHttpRoute[R <: Has[_]](zioRoute: URIO[R, Route]) {
    def toAkkaRoute[E](implicit layer: Layer[E, R]): Route = {
      val routeWithDependencies: ZIO[Any, E, Route] = zioRoute.provideLayer(layer)
      zio.Runtime.default.unsafeRun(routeWithDependencies)
    }
  }
}
