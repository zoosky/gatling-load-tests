package bbc.loadtest.sample

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SampleTest extends Simulation {

  val httpProtocol = http
    .baseURL("http://computer-database.herokuapp.com")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-gb,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:31.0) Gecko/20100101 Firefox/31.0""")

  val scn = scenario("Computers Database")
    .exec(http("computers")
    .get("/computers")
    .check(status.is(200)))

  setUp(scn.inject(
    constantUsersPerSec(3) during(10 seconds)
  ).protocols(httpProtocol)) 
}
