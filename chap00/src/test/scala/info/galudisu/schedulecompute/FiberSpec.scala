package info.galudisu.schedulecompute

import info.galudisu.schedulecompute.Fiber._
import zio.duration._
import zio.test.TestAspect._
import zio.test._
import zio.test.environment._

object FiberSpec extends DefaultRunnableSpec {
  def spec: ZSpec[TestEnvironment, Any] = {
    suite("FiberSpec")(
      testM("concurrent wake up routine") {
        for {
          _ <- concurrentWakeUpRoutine().fork
        } yield assertCompletes
      },
      testM("concurrent wake up routine with alice call") {
        for {
          fiber <- concurrentWakeUpRoutineWithAliceCall().fork
          _     <- fiber.join
        } yield assertCompletes
      },
      testM("concurrent wake up routine with alice calling us too late") {
        for {
          fiber <- concurrentWakeUpRoutineWithAliceCallingUsTooLate().fork
          _     <- TestClock.adjust(5.seconds)
          _     <- fiber.join
        } yield assertCompletes
      }
    ) @@ sequential
  }

}
