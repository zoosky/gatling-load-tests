package homepage

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HomePageV5 extends Simulation {

  val weatherFeeder = csv("homepage/geonames_domestic.csv").random

  val headers = Map(
    "Cache-Control" -> "no-cache",
    "Pragma" -> "no-cache"
  )

  val httpProtocol = http
    .baseURL("https://homepage.cloud.bbc.co.uk")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-GB,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:33.0) Gecko/20100101 Firefox/33.0""")
    .headers(headers)

  val scn = scenario("Three Day Weather")
    .feed(weatherFeeder)
    .exec(http("homepage")
    .get("/").check(status.is(200))
    .resources(http("three day weather")
    .get("/home/five/modules/weather/threeday/en/${geoname_id}")
    .check(status.is(200))))

    setUp(scn.inject(
      rampUsersPerSec(10) to(150) during(3 minutes),
      constantUsersPerSec(150) during(17 minutes)
    ).protocols(httpProtocol))
}
