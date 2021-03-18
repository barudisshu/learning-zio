package info.galudisu.netty
import zio._
import zio.console._

object Main extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (for {
      _ <- putStrLn("hello world")
    } yield ()).exitCode
}
