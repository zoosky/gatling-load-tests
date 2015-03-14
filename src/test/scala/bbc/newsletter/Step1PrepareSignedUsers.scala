package bbc.newsletter

import java.io._
import java.util.concurrent.ConcurrentLinkedQueue
import scala.collection.JavaConversions._
import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.cookie._

class Step1PrepareSignedUsers extends Simulation {

  val httpProtocol = http
    .baseURL("https://ssl.stage.bbc.co.uk")
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptLanguageHeader("fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36")

  val cookieJars = new ConcurrentLinkedQueue[CookieJar]

  import java.util.concurrent.atomic._
  val value = new AtomicInteger(1)

  val prepareSignedUsers = scenario("prepareSignedUsers")
    
    .exec(http("landingPage").get("/newsletters/thenolanshow"))
    
    .exec(http("signIn").get("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow"))
    
    .exec(http("signInPost").post("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
      .formParam("unique", session => s"adrian@loadtest${value.getAndIncrement}.com")
      .formParam("password", "loadtest")
      .formParam("rememberme", "1")
      .formParam("bbcid_submit_button", "Sign in")
        .check(substring("Please confirm your age")))
    // drop non persistent cookies
    .exec(flushSessionCookies)

    // save the cookieJar in the global queue
    .exec { session =>
      CookieHandling.cookieJar(session).foreach(cookieJars.add)
      session
    }

  setUp(prepareSignedUsers.inject(
    rampUsersPerSec(10) to(20) during(5 minutes),
    constantUsersPerSec(20) during(5 minutes)
  ).protocols(httpProtocol))

  after {
    CookieJarHelper.dumpCookieJarFeeder(cookieJars)
  }
}
