package bbc.loadtest.worldwidetravel

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

  val travelStoryFeed = csv("worldwide-travel/travel_story.csv").circular
  val travelTagPageFeed = csv("worldwide-travel/travel_tagpage.csv").circular

  val scn = scenario("Worldwide Travel")
    .feed(travelStoryFeed)
    .exec(http("travel story")
    .get("${travelStoryUrl}")
    .check(status.is(200)))

    .exec(http("travel homepage")
    .get("/travel")
    .check(status.is(200)))

    .feed(travelTagPageFeed)
    .exec(http("travel tag pages")
    .get("${travelTagpageUrl}")
    .check(status.is(200)))

    .exec(http("destinations index page")
    .get("/travel/destinations")
    .check(status.is(200)))

  setUp(scn.inject(
    rampUsersPerSec(4) to(8) during(1 minutes),
    constantUsersPerSec(8) during(8 minutes),
    rampUsersPerSec(8) to(35) during(1 minutes),
    constantUsersPerSec(35) during(1 minutes)
  ).protocols(httpProtocol))
}

