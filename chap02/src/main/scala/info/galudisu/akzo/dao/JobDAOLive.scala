package info.galudisu.akzo.dao

import doobie.implicits._
import info.galudisu.akzo.model._
import info.galudisu.akzo.{Dependencies, IOTransactor}
import zio.IO
import zio.interop.catz._

class JobDAOLive(val xa: IOTransactor) extends Dependencies.JobDAO.Service {
  override def currentJob(jobId: Long): IO[JobError, Job] = {
    val jobDatabaseResult =
      sql"""
            SELECT * FROM t_job where id = $jobId
        """.query[Job].option

    jobDatabaseResult.transact(xa).mapError(JobDBAccessError).flatMap {
      case Some(job) => IO.succeed(job)
      case None      => IO.fail(JobNotFound)
    }
  }
}
