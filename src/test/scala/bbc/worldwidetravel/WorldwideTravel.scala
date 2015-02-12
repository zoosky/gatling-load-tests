
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

  val travel_story_feed = csv("worldwide-travel/travel_story.csv").circular
  val travel_tagpage_feed = csv("worldwide-travel/travel_tagpage.csv").circular


  val scn = scenario("Worldwide Travel")
    .feed(travel_story_feed)
    .feed(travel_tagpage_feed)

    .randomSwitch(
      25d -> exec(http("travel story")
        .get("${travel_story_urls")
        .check(status.is(200)))



  25d -> group("HomePage") {
    exec(http("travel homepage")
      .get("/travel")
      .check(status.is(200)))

  }

  25d -> group("Tag Page") {
    exec(http("travel tag pages")
      .get("${travel_tagpage_urls}")
      .check(status.is(200)))

  }

  25d -> group("Destinations Index Page") {
    exec(http("destinations index page")
      .get("/travel/destinations")
      .check(status.is(200)))

  }

  )


  setUp(scn.inject(
    rampUsersPerSec(4) to(8) during(2 minutes),
    constantUsersPerSec(8) during(8 minutes),
    rampUsersPerSec(8) to(38) during(2 minutes),
    constantUsersPerSec(38) during(3 minutes)
  ).protocols(httpProtocol))
}

