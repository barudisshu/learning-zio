package info.galudisu.schedulecompute

import info.galudisu.schedulecompute.Fiber._
import zio.test._
import zio.test.environment._

object FiberSpec extends DefaultRunnableSpec {
  def spec: ZSpec[TestEnvironment, Unit] =
    suite("FiberSpec")(testM("concurrent wake up routine") {
      for {
        _ <- concurrentWakeUpRoutine()
      } yield assertTrue(true)
    })
}
