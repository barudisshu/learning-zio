package info.galudisu.crawler

import zio.{ExitCode, IO, URIO}

object Main extends zio.App {

  val Home: URL          = URL.make("https://zio.dev").get
  val Index: URL         = URL.make("https://zio.dev/index.html").get
  val ScaladocIndex: URL = URL.make("https://zio.dev/scaladoc/index.html").get
  val About: URL         = URL.make("https://zio.dev/about").get

  val TestRouter: URL => Set[URL] =
    url => if (url.parsed.apexDomain.contains("zio.dev")) Set(url) else Set()

  val Processor: (URL, String) => IO[List[(URL, String)], Unit] =
    (url, html) => IO.succeed(List(url -> html))

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    crawl(Set(Home, Index, ScaladocIndex, About), TestRouter, Processor).exitCode

}
