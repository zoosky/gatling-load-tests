package bbc.news
import scala.concurrent.duration._
 
import io.gatling.core.Predef._
import io.gatling.http.Predef._
 
class NewsbeatLive extends Simulation {
 
  val httpProtocol = http
      .baseURL("http://www.stage.bbc.co.uk")
      .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*171""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
      .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
      .acceptEncodingHeader("""gzip, deflate""")
      .acceptLanguageHeader("""en-gb,en;q=0.5""")
      .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:31.0) Gecko/20100101 Firefox/31.0""")
 
  val scn = scenario("Newsbeat Test")
      .exec(http("/newsbeat")
      .get("/newsbeat")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/popular")
      .get("/newsbeat/popular")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/topics")
      .get("/newsbeat/topics")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/topics/music")
      .get("/newsbeat/topics/music")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/topics/entertainment")
      .get("/newsbeat/topics/entertainment")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/story")
      .get("/newsbeat/article/32129276/pilot-shares-thank-you-letter-from-airline-passenger")
      .check(status.is(200)))
 
      .exec(http("/newsbeat/story2")
      .get("/newsbeat/article/32220611")
      .check(status.is(200)))
 
  setUp(scn.inject(
    rampUsersPerSec(1) to(25) during(2 minutes),
    constantUsersPerSec(25) during(8 minutes) randomized,
    rampUsersPerSec(25) to(50) during(2 minutes),
    constantUsersPerSec(50) during(8 minutes),
    rampUsersPerSec(50) to(125) during(2 minutes),
    constantUsersPerSec(125) during(8 minutes),
    rampUsersPerSec(125) to(400) during(5 minutes),
    constantUsersPerSec(400) during(15 minutes)
  ).protocols(httpProtocol))
}
