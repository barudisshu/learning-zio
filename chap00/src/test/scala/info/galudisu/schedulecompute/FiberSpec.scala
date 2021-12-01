package info.galudisu.schedulecompute

import info.galudisu.schedulecompute.Fiber._
import zio.test.{DefaultRunnableSpec, TestSuccess}

object FiberSpec extends DefaultRunnableSpec {
  def spec =
    suite("FiberSpec")(testM("concurrent wake up routine") {
      for {
        r <- concurrentWakeUpRoutine()
      } yield assert(TestSuccess.Succeeded)
    })
}
