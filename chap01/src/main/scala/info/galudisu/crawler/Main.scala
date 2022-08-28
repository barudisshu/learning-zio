package info.galudisu.crawler

import zio.*

object Main extends ZIOAppDefault {

  val Home: URL          = URL.make("http://zio.dev").get
  val Index: URL         = URL.make("http://zio.dev/index.html").get
  val ScaladocIndex: URL = URL.make("http://zio.dev/scaladoc/index.html").get
  val About: URL         = URL.make("http://zio.dev/about").get

  val TestRouter: URL => Set[URL] =
    url => if url.parsed.apexDomain.contains("zio.dev") then Set(url) else Set()

  val Processor: (URL, String) => IO[List[(URL, String)], Unit] =
    (url, html) => ZIO.succeed(List(url -> html))

  def run: URIO[Any, ExitCode] = crawl(Set(Home, Index, ScaladocIndex, About), TestRouter, Processor).exitCode
}
