//package info.galudisu.schedulecompute
//
//import info.galudisu.schedulecompute.Fiber.*
//import zio.Clock.*
//import zio.internal.stacktracer.Tracer
//import zio.test.*
//import zio.test.TestAspect.*
//import zio.{Clock, Scope, ZLayer, durationInt}
//
//object FiberSpec extends ZIOSpecDefault:
//
//  def spec: Spec[Environment & TestEnvironment, Any] =
//    suite("FiberSpec")(
//      test("concurrent wake up routine") {
//          for {
//            _ <- concurrentWakeUpRoutine().fork
//          } yield assertCompletes
//      },
//      test("concurrent wake up routine with alice call") {
//        for {
//          fiber <- concurrentWakeUpRoutineWithAliceCall().fork
//          _     <- fiber.join
//        } yield assertCompletes
//      },
//      test("concurrent wake up routine with alice calling us too late") {
//        for {
//          fiber <- concurrentWakeUpRoutineWithAliceCallingUsTooLate().fork
//          _     <- TestClock.adjust(5.seconds)
//          _     <- fiber.join
//        } yield assertCompletes
//      }
//    ) @@ sequential @@ TestAspect.withLiveClock
