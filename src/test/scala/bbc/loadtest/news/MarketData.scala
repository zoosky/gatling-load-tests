package bbc.loadtest.news 
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MarketData extends Simulation {

  val httpProtocol = http
    .baseURL("http://pal.stage.bbc.co.uk/news")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*171""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-gb,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:31.0) Gecko/20100101 Firefox/31.0""")

  val scn = scenario("Market Data")
    .exec(http("London Stock Exchange")
    .get("/business/markets/europe/lse_ukx")
    .check(status.is(200)))

    .exec(http("Markets Overview")
    .get("/business/markets/europe/overview")
    .check(status.is(200)))
    
    .exec(http("Global Markets")
    .get("/business/markets/global")
    .check(status.is(200)))

    .exec(http("Promo")
    .get("/business")
    .check(status.is(200)))

  setUp(scn.inject(
    rampUsersPerSec(1) to(25) during(2 minutes),
    constantUsersPerSec(25) during(18 minutes)
  ).protocols(httpProtocol)) 
}
