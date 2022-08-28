package info.galudisu.crawler

import scala.util.Try

/**
 * A data structure representing a structured URL, with a smart constructor.
 */
final case class URL private (parsed: io.lemonlabs.uri.Url) {
  import io.lemonlabs.uri.*
  def relative(page: String): Option[URL] =
    Try(parsed.path match {
      case Path(parts) =>
        val whole = parts.dropRight(1) :+ page.dropWhile(_ == '/')
        parsed.withPath(UrlPath(whole))
      case _ =>
        throw new IllegalArgumentException("illegal argument")
    }).toOption.map(new URL(_))
  def url: String = parsed.toString

  override def equals(a: Any): Boolean = a match {
    case that: URL => this.url == that.url
    case _         => false
  }
  override def hashCode: Int = url.hashCode
}

object URL {
  import io.lemonlabs.uri.*
  def make(url: String): Option[URL] =
    Try(AbsoluteUrl.parse(url)).toOption match {
      case None         => None
      case Some(parsed) => Some(new URL(parsed))
    }
}
