package bbc.thenolanshow

import java.io._

import com.ning.http.client.cookie.Cookie

import io.gatling.core.Predef._
import io.gatling.core.config.GatlingFiles
import io.gatling.core.util.IO
import io.gatling.http.cookie._

object CookieJarHelper {

  private val CookiesFeederFileName = "cookies.tsv"
  private val CookiesSeparator = "€"
  private val CookieFieldsSeparator = "£"

  // could be using Jackson or MsgPack here, but went to no dependency quick solution
  private def encodeCookieJar(cookieJar: CookieJar): String =
    cookieJar.store.map {
      case (cookieKey, storedCookie) =>
        List(
          cookieKey.name,
          cookieKey.domain,
          cookieKey.path,
          storedCookie.cookie.getName,
          storedCookie.cookie.getValue,
          storedCookie.cookie.getDomain,
          storedCookie.cookie.getRawValue,
          storedCookie.cookie.getPath,
          storedCookie.cookie.getExpires,
          storedCookie.cookie.getMaxAge,
          storedCookie.cookie.isSecure,
          storedCookie.cookie.isHttpOnly,
          storedCookie.hostOnly,
          storedCookie.persistent,
          storedCookie.creationTime).mkString(CookieFieldsSeparator)
    }.mkString(CookiesSeparator)

  private def decodeCookieJar(cookiesString: String): CookieJar = {

    val cookieStrings = cookiesString.split(CookiesSeparator)
    val cookieStore = cookieStrings.map(_.split(CookieFieldsSeparator).toSeq).map {
      case Seq(cookieKeyName,
        cookieKeyDomain,
        cookieKeyPath,
        storedCookieName,
        storedCookieValue,
        storedCookieDomain,
        storedCookieRawValue,
        storedCookiePath,
        storedCookieExpires,
        storedCookieMaxAge,
        storedCookieSecure,
        storedCookieHttpOnly,
        storedCookieHostOnly,
        storedCookiePersistent,
        storedCookieCreationTime) =>

        val cookieKey = CookieKey(cookieKeyName, cookieKeyDomain, cookieKeyPath)
        val cookie = new Cookie(
          storedCookieName,
          storedCookieValue,
          storedCookieRawValue,
          storedCookieDomain,
          storedCookiePath,
          storedCookieExpires.toLong,
          storedCookieMaxAge.toInt,
          storedCookieSecure.toBoolean,
          storedCookieHttpOnly.toBoolean)

        cookieKey -> StoredCookie(cookie, storedCookieHostOnly.toBoolean, storedCookiePersistent.toBoolean, storedCookieCreationTime.toLong)
    }.toMap

    CookieJar(cookieStore)
  }

  def dumpCookieJarFeeder(cookieJars: Iterable[CookieJar]): Unit =
    // just a loan pattern to automatically close the writer
    IO.withCloseable {
      val tsvFile = new File(GatlingFiles.dataDirectory.toFile, CookiesFeederFileName)
      new PrintWriter(new FileWriter(tsvFile))
    } { writer =>

      // header (use the same name has the standard CookieJar attribute, so we can later directly feed it)
      writer.println(CookieHandling.CookieJarAttributeName)
      // records
      cookieJars.foreach { cookieJar => writer.println(CookieJarHelper.encodeCookieJar(cookieJar)) }
    }

  // use tsv convert method to decode the cookeJar
  def cookieJarFeeder = tsv(CookiesFeederFileName).convert {
    case (CookieHandling.CookieJarAttributeName, cookiesString) => decodeCookieJar(cookiesString)
  }
}
