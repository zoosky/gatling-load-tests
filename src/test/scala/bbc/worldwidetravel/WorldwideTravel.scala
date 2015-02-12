package bbc.worldwidetravel

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class WorldwideTravel extends Simulation {

  val httpProtocol = http
    .baseURL("http://www.stage.bbc.com")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-gb,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:31.0) Gecko/20100101 Firefox/31.0""")

  val travelUrl = csv("worldwide-travel/worldwidetravelurls.csv").circular

  val scn = scenario("Worldwide Travel")
    .feed(travelUrl)
    .exec(http("Travel v2")
    .get("${travelurls}")
    .check(status.is(200)))

  setUp(scn.inject(
    rampUsersPerSec(10) to(30) during(2 minutes),
    constantUsersPerSec(30) during(8 minutes),
    rampUsersPerSec(30) to(150) during(2 minutes),
    constantUsersPerSec(150) during(3 minutes)
  ).protocols(httpProtocol))
}
