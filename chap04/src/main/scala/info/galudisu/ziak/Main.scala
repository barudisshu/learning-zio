package info.galudisu.ziak
import info.galudisu.ziak.Channel.Channel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.{ReflectiveChannelFactory, Channel => JChannel}
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console._

import java.net.InetSocketAddress

object Main extends App {

  val workerGroup                  = new NioEventLoopGroup()
  val channel: JChannel            = new ReflectiveChannelFactory[JChannel](classOf[NioServerSocketChannel]).newChannel()
  val liveChannel: ULayer[Channel] = Channel.live(channel)

  val env = Console.live ++ Clock.live ++ Blocking.live ++ liveChannel ++ (liveChannel >>> Channel.Unsafe.live)

  val program: ZIO[Console with Clock with Blocking with Channel with Channel.Unsafe, Throwable, Unit] = {

    def loop(): ZIO[Console with Blocking with Channel with Channel.Unsafe, Throwable, Unit] = {
      {
        for {
          jChannel <- Channel.read()
          _        <- Channel.close()
        } yield jChannel
      } *> loop()
    }

    for {
      _              <- Channel.Unsafe.beginRead()
      channelPromise <- Channel.Unsafe.voidPromise()
      _              <- Channel.Unsafe.register(workerGroup.next(), channelPromise)
      _              <- Channel.Unsafe.bind(new InetSocketAddress(8080), channelPromise)
      _              <- loop()
      _              <- Channel.eventLoop.map(_.shutdownGracefully())
    } yield ()
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    program.provideLayer(env).exitCode
}
