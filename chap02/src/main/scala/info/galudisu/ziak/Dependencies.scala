package info.galudisu.ziak

import doobie.util.transactor.Transactor
import info.galudisu.ziak.dao.JobDAOLive
import info.galudisu.ziak.model._
import zio._
import zio.clock.Clock
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts

object Dependencies {
  type ExtServices = JobDAO with Clock
  type JobDAO      = Has[JobDAO.Service]

  object JobDAO {
    trait Service {
      def currentJob(jobId: Long): IO[JobError, Job]
    }
    val live: ULayer[JobDAO] =
      ZLayer.succeed {
        val xa = Transactor.fromDriverManager[Task](
          "org.postgresql.Driver",
          "jdbc:postgresql://localhost:5432/test",
          "postgres",
          ""
        )
        new JobDAOLive(xa)
      }
  }
  val extServicesLive: ULayer[ExtServices] = JobDAO.live ++ Clock.live
}
