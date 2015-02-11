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

  val scn = scenario("Worldwide Travel")
    .exec(http("worldwidetravel")
    .get("/travel/story/20150112-the-gateway-to-kilimanjaro")
    .check(status.is(200)))

  setUp(scn.inject(
    constantUsersPerSec(2) during(300 seconds)
  ).protocols(httpProtocol))
}
