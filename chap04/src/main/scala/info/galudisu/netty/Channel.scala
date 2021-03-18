package info.galudisu.netty

import io.netty.buffer.ByteBufAllocator
import io.netty.channel.{ChannelConfig, ChannelId, ChannelMetadata, ChannelOutboundBuffer, ChannelPipeline, ChannelPromise, EventLoop, Channel => JChannel}
import zio._

import java.lang.{Object => JObject}
import java.net.{SocketAddress => JSocketAddress}

object Channel {

  type Channel = Has[Service]
  type Unsafe = Has[Unsafe.Service]

  trait Service {
    def alloc(): Task[ByteBufAllocator]
    def bytesBeforeUnwritable(): Task[Long]
    def bytesBeforeWritable(): Task[Long]
    def close(): Task[Unit]
    def config: Task[ChannelConfig]
    def eventLoop: Task[EventLoop]
    def id: Task[ChannelId]
    def isActive: Task[Boolean]
    def isOpen: Task[Boolean]
    def isRegistered: Task[Boolean]
    def isWritable: Task[Boolean]
    def localAddress(): Task[JSocketAddress]
    def metaData(): Task[ChannelMetadata]
    def parent(): Task[JChannel]
    def pipeline(): Task[ChannelPipeline]
    def read(): Task[JChannel]
    def remoteAddress(): Task[JSocketAddress]
    def unsafe(): Task[JChannel.Unsafe]
  }

  def live(channel: JChannel): ULayer[Channel] = ZLayer.succeed {
    new Service {
      private def doSomethingWithChannel[A](f: JChannel => A): Task[A] =
        Task(f(channel))
      def alloc(): Task[ByteBufAllocator] =
        doSomethingWithChannel(_.alloc())
      def bytesBeforeUnwritable(): Task[Long] =
        doSomethingWithChannel(_.bytesBeforeUnwritable())
      def bytesBeforeWritable(): Task[Long] =
        doSomethingWithChannel(_.bytesBeforeWritable())
      def close(): Task[Unit] =
        doSomethingWithChannel(_.close())
      val config: Task[ChannelConfig] =
        doSomethingWithChannel(_.config())
      val eventLoop: Task[EventLoop] =
        doSomethingWithChannel(_.eventLoop())
      val id: Task[ChannelId] =
        doSomethingWithChannel(_.id())
      val isActive: Task[Boolean] =
        doSomethingWithChannel(_.isActive())
      val isOpen: Task[Boolean] =
        doSomethingWithChannel(_.isOpen())
      val isRegistered: Task[Boolean] =
        doSomethingWithChannel(_.isRegistered())
      val isWritable: Task[Boolean] =
        doSomethingWithChannel(_.isWritable())
      def localAddress(): Task[JSocketAddress] =
        doSomethingWithChannel(_.localAddress())
      def metaData(): Task[ChannelMetadata] =
        doSomethingWithChannel(_.metadata())
      def parent(): Task[JChannel] =
        doSomethingWithChannel(_.parent())
      def pipeline(): Task[ChannelPipeline] =
        doSomethingWithChannel(_.pipeline())
      def read(): Task[JChannel] =
        doSomethingWithChannel(_.read())
      def remoteAddress(): Task[JSocketAddress] =
        doSomethingWithChannel(_.remoteAddress())
      def unsafe(): Task[JChannel.Unsafe] =
        doSomethingWithChannel(_.unsafe())
    }
  }

  private def getEnv[R, E, A](f: Channel.Service => Task[A]): RIO[Channel, A] =
    RIO.accessM(env => f(env.get))

  def alloc(): RIO[Channel, ByteBufAllocator] =
    getEnv(_.alloc())
  def bytesBeforeUnwritable(): RIO[Channel, Long] =
    getEnv(_.bytesBeforeUnwritable())
  def bytesBeforeWritable(): RIO[Channel, Long] =
    getEnv(_.bytesBeforeWritable())
  def close(): RIO[Channel, Unit] =
    getEnv(_.close())
  def config: RIO[Channel, ChannelConfig] =
    getEnv(_.config)
  def eventLoop: RIO[Channel, EventLoop] =
    getEnv(_.eventLoop)
  def id: RIO[Channel, ChannelId] =
    getEnv(_.id)
  def isActive: RIO[Channel, Boolean] =
    getEnv(_.isActive)
  def isOpen: RIO[Channel, Boolean] =
    getEnv(_.isOpen)
  def isRegistered: RIO[Channel, Boolean] =
    getEnv(_.isRegistered)
  def isWritable: RIO[Channel, Boolean] =
    getEnv(_.isWritable)
  def localAddress(): RIO[Channel, JSocketAddress] =
    getEnv(_.localAddress())
  def metaData(): RIO[Channel, ChannelMetadata] =
    getEnv(_.metaData())
  def parent(): RIO[Channel, JChannel] =
    getEnv(_.parent())
  def pipeline(): RIO[Channel, ChannelPipeline] =
    getEnv(_.pipeline())
  def read(): RIO[Channel, JChannel] =
    getEnv(_.read())
  def remoteAddress(): RIO[Channel, JSocketAddress] =
    getEnv(_.remoteAddress())
  def unsafe(): RIO[Channel, JChannel.Unsafe] =
    getEnv(_.unsafe())

  object Unsafe {
    trait Service {
      def beginRead(): Task[Unit]
      def bind(address: JSocketAddress, promise: ChannelPromise): Task[Unit]
      def close(promise: ChannelPromise): Task[Unit]
      def closeForcibly(): Task[Unit]
      def connect(remote: JSocketAddress, local: JSocketAddress, promise: ChannelPromise): Task[Unit]
      def deregister(promise: ChannelPromise): Task[Unit]
      def disconnect(promise: ChannelPromise): Task[Unit]
      def flush(): Task[Unit]
      def localAddress(): Task[JSocketAddress]
      def outboundBuffer(): Task[ChannelOutboundBuffer]
      def register(eventLoop: EventLoop, promise: ChannelPromise): Task[Unit]
      def remoteAddress(): Task[JSocketAddress]
      def voidPromise(): Task[ChannelPromise]
      def write(msg: JObject, promise: ChannelPromise): Task[Unit]
    }

    private def getEnv[A](f: Unsafe.Service => Task[A]): RIO[Unsafe, A] =
      RIO.accessM(env => f(env.get))

    def beginRead(): RIO[Unsafe, Unit] =
      getEnv(_.beginRead())
    def bind(address: JSocketAddress, promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.bind(address, promise))
    def close(promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.close(promise))
    def closeForcibly(): RIO[Unsafe, Unit] =
      getEnv(_.closeForcibly())
    def connect(remote: JSocketAddress, local: JSocketAddress, promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.connect(remote, local, promise))
    def deregister(promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.deregister(promise))
    def disconnect(promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.disconnect(promise))
    def flush(): RIO[Unsafe, Unit] =
      getEnv(_.flush())
    def localAddress(): RIO[Unsafe, JSocketAddress] =
      getEnv(_.localAddress())
    def outboundBuffer(): RIO[Unsafe, ChannelOutboundBuffer] =
      getEnv(_.outboundBuffer())
    def register(eventLoop: EventLoop, promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.register(eventLoop, promise))
    def remoteAddress(): RIO[Unsafe, JSocketAddress] =
      getEnv(_.remoteAddress())
    def voidPromise(): RIO[Unsafe, ChannelPromise] =
      getEnv(_.voidPromise())
    def write(msg: JObject, promise: ChannelPromise): RIO[Unsafe, Unit] =
      getEnv(_.write(msg, promise))

    def live: ZLayer[Channel, Nothing, Unsafe] = ZLayer.fromFunction { channel: Channel =>
      new Service {
        private def getUnsafeChannelThenExec[A](f: JChannel.Unsafe => A): Task[A] = for {
          unsafeChannel <- channel.get.unsafe()
          res <- Task(f(unsafeChannel))
        } yield res
          
        def beginRead(): Task[Unit] =
          getUnsafeChannelThenExec(_.beginRead())
        def bind(address: JSocketAddress, promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.bind(address, promise))
        def close(promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.close(promise))
        def closeForcibly(): Task[Unit] =
          getUnsafeChannelThenExec(_.closeForcibly())
        def connect(remote: JSocketAddress, local: JSocketAddress, promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.beginRead())
        def deregister(promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.deregister(promise))
        def disconnect(promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.disconnect(promise))
        def flush(): Task[Unit] =
          getUnsafeChannelThenExec(_.flush())
        def outboundBuffer(): Task[ChannelOutboundBuffer] =
          getUnsafeChannelThenExec(_.outboundBuffer())
        def localAddress(): Task[JSocketAddress] =
          getUnsafeChannelThenExec(_.localAddress())
        def register(eventLoop: EventLoop, promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.register(eventLoop, promise))
        def remoteAddress(): Task[JSocketAddress] =
          getUnsafeChannelThenExec(_.remoteAddress())
        def voidPromise(): Task[ChannelPromise] =
          getUnsafeChannelThenExec(_.voidPromise())
        def write(msg: JObject, promise: ChannelPromise): Task[Unit] =
          getUnsafeChannelThenExec(_.write(msg, promise))
      }
    }
  }

}