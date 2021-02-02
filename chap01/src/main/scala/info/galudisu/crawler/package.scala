package info.galudisu

import zio.blocking.Blocking
import zio.{IO, Ref, ZIO}

package object crawler {
  final case class CrawlState[+E](visited: Set[URL], errors: List[E]) {
    def visitAll(urls: Set[URL]): CrawlState[E]  = copy(visited = visited ++ urls)
    def logError[E1 >: E](e: E1): CrawlState[E1] = copy(errors = e :: errors)
  }

  def crawl[E](
      seeds: Set[URL],
      router: URL => Set[URL],
      processor: (URL, String) => IO[E, Unit]
  ): ZIO[Blocking, Nothing, List[E]] = {
    def loop(seeds: Set[URL], ref: Ref[CrawlState[E]]): ZIO[Blocking, Nothing, Unit] =
      ZIO
        .foreach(seeds) { seed =>
          (for {
            html <- getURL(seed)
            scraped = extractURLs(seed, html).toSet.flatMap(router)
            either <- processor(seed, html).either
            newUrls <- ref.modify(state =>
              (scraped -- state.visited, { val s2 = state.visitAll(scraped); either.fold(s2.logError, _ => s2) }))
          } yield newUrls) orElse ZIO.succeed(Set.empty[URL])
        }
        .map(_.flatten)
        .flatMap(newUrls => loop(newUrls, ref))
    for {
      ref   <- Ref.make(CrawlState(seeds, List.empty[E]))
      _     <- loop(seeds, ref)
      state <- ref.get
    } yield state.errors
  }

  def getURL(url: URL): ZIO[Blocking, Throwable, String] = {
    import scala.util.{Try, Using}
    def getURLImpl(url: URL): Try[String] = Using(scala.io.Source.fromURL(url.url)(scala.io.Codec.UTF8)) { _.mkString }
    ZIO.fromTry(getURLImpl(url))
  }

  /**
    * A function that extracts URLs from a given web page.
    */
  def extractURLs(root: URL, html: String): List[URL] = {
    val pattern = "href=[\"\']([^\"\']+)[\"\']".r
    scala.util
      .Try({
        val matches = (for (m <- pattern.findAllMatchIn(html)) yield m.group(1)).toList
        for {
          m   <- matches
          url <- URL.make(m).toList ++ root.relative(m).toList
        } yield url
      })
      .getOrElse(Nil)
  }
}
