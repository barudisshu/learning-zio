package info.galudisu.akzo

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import info.galudisu.akzo.Dependencies._
import info.galudisu.akzo.model._
import io.circe.Json
import io.circe.generic.auto._
import zio.{IO, ULayer, URIO, ZIO}

class AkkaHttpService(implicit val serviceLayer: ULayer[ExtServices]) extends ErrorAccumulatingCirceSupport {

  val jobDao: URIO[JobDAO, JobDAO.Service] = ZIO.service[Dependencies.JobDAO.Service]

  val getJobRoute: Route = pathPrefix("job") {
    path(LongNumber) { jobId =>
      val jobDbResult: ZIO[ExtServices, JobError, Job] = for {
        dao    <- jobDao
        job    <- dao.currentJob(jobId)
        result <- IO.fromEither(Job.validate(job))
      } yield result

      jobOrErrorResponse(jobDbResult).toAkkaRoute
    }
  }
  val routes: Route = concat(getJobRoute /*, ...*/ )

  def jobOrErrorResponse(jobResponse: ZIO[ExtServices, JobError, Job]): URIO[ExtServices, Route] = {
    jobResponse.fold(
      {
        case EmptyJob    => complete(Json.fromString("Job is empty"))
        case JobNotFound => complete(HttpResponse(StatusCodes.NotFound))
        case jobError    => failWith(jobError)
      },
      job => complete(job)
    )
  }

}
