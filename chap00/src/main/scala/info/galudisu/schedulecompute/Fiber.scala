package info.galudisu.schedulecompute

import zio._
import zio.clock.Clock
import zio.duration.durationInt

object Fiber {

  def printThread = s"[${Thread.currentThread().getName}]"

  val bathTime: UIO[String]        = ZIO.succeed("Going to the bathroom")
  val boilingWater: UIO[String]    = ZIO.succeed("Boiling some water")
  val preparingCoffee: UIO[String] = ZIO.succeed("Preparing the coffee")

  def concurrentWakeUpRoutine(): URIO[Clock, Unit] =
    for {
      bathFiber    <- bathTime.debug(printThread).fork
      boilingFiber <- boilingWater.debug(printThread).fork
      zippedFiber = bathFiber.zip(boilingFiber)
      result <- zippedFiber.join.debug(printThread)
      _      <- ZIO.succeed(s"$result...done").debug(printThread) *> preparingCoffee.debug(printThread)
    } yield ()

  val aliceCalling: UIO[String] = ZIO.succeed("Alice's call")

  val boilingWaterWithSleep: URIO[Clock, String] =
    (boilingWater.debug(printThread) *> ZIO.sleep(5.seconds)).as("Boiled water ready")

  def concurrentWakeUpRoutineWithAliceCall(): ZIO[Clock, Nothing, Unit] =
    for {
      _            <- bathTime.debug(printThread)
      boilingFiber <- boilingWaterWithSleep.fork
      _            <- aliceCalling.debug(printThread).fork *> boilingFiber.interrupt.debug(printThread)
      _            <- ZIO.succeed("Going to the Cafe with Alice").debug(printThread)
    } yield ()

  val preparingCoffeeWithSleep: URIO[Clock, String] =
    (preparingCoffee.debug(printThread) *> ZIO.sleep(5.seconds)).as("Coffee ready")

  def concurrentWakeUpRoutineWithAliceCallingUsTooLate(): URIO[Clock, Unit] =
    for {
      _           <- bathTime.debug(printThread)
      _           <- boilingWater.debug(printThread)
      coffeeFiber <- preparingCoffeeWithSleep.debug(printThread).fork.uninterruptible
      result      <- aliceCalling.debug(printThread).fork *> coffeeFiber.interrupt.debug(printThread)
      _ <- result match {
        case Exit.Success(_) => ZIO.succeed("Making breakfast at home").debug(printThread)
        case Exit.Failure(_) => ZIO.succeed("Going to the Cafe with Alice").debug(printThread)
      }
    } yield ()

}
