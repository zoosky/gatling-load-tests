package bbc.memcache

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Memcache extends Simulation {
    val httpProtocol = http
        .baseURL("http://www.stage.bbc.co.uk")
        .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
        .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
        .acceptEncodingHeader("""gzip, deflate""")
        .acceptLanguageHeader("""en-GB,en;q=0.5""")
        .connection("""keep-alive""")
        .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:33.0) Gecko/20100101 Firefox/33.0""")

    val scn = scenario("Memcache")
        .exec(http("homepage")
        .get("/")
        .check(status.is(200)))
        .exec(http("food")
        .get("/food")
        .check(status.is(200)))
        .exec(http("sign-in")
        .get("/id/signin")
        .check(status.is(200)))

    setUp(scn.inject(
        rampUsersPerSec(10) to(300) during(3 minutes),
        constantUsersPerSec(300) during(17 minutes)
    ).protocols(httpProtocol))
}
