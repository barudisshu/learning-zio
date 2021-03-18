package info.galudisu.netty.model

case class Job(id: Long, name: String)

sealed abstract class JobError(cause: Throwable) extends Exception(cause)
case object EmptyJob                             extends JobError(new Exception)
case object JobNotFound                          extends JobError(new Exception)
case class JobDBAccessError(cause: Throwable)    extends JobError(cause)

object Job {
  def validate(job: Job): Either[JobError, Job] = if (true) Right(job) else Left(EmptyJob)
}
