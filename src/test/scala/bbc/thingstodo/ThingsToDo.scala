package bbc.thingstodo

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ThingsToDo extends Simulation { 

    val httpProtocol = http
        .baseURL("https://api.stage.bbc.co.uk")
        .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
        .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
        .acceptEncodingHeader("""gzip, deflate""")
        .acceptLanguageHeader("""en-gb,en;q=0.5""")
        .connection("""keep-alive""")
        .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:31.0) Gecko/20100101 Firefox/31.0""")

    val sportFeeder = csv("thingstodo/linked-data-sport-api.txt").circular

    val scn = scenario("LDP Sport")
        .feed(sportFeeder)
        .exec(http("LDP Sport")
        .get("${sport}")
        .check(status.is(200)))
      
    setUp(scn.inject(
        rampUsersPerSec(10) to(160) during(5 minutes),
        constantUsersPerSec(160) during(10 minutes),
        rampUsersPerSec(160) to(500) during(5 minutes),
        constantUsersPerSec(500) during(5 minutes)
    ).protocols(httpProtocol))
}


