package bbc.music

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MusicClip extends Simulation {

  val httpProtocol = http
    .baseURL("http://www.stage.bbc.co.uk")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-US,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:32.0) Gecko/20100101 Firefox/32.0""")

  val scn = scenario("MusicClip")


    .group("T01_Popular") {
    exec(http("popular").get("""/music/playlister/popular""").check(status.is(200)))}
    .group("T02_Recent") {
    exec(http("recent").get("""/music/playlister/recent""").check(status.is(200)))}
    .group("T03_PresenterTracks") {
    exec(http("tracks").get("""/music/playlister/presentertracks""").check(status.is(200)))}
    .group("T04_Presenters") {
    exec(http("presenters").get("""/music/playlister/presenters""").check(status.is(200)))}


  setUp(scn.inject(
    rampUsersPerSec(1) to (4) during(2 minutes),
    constantUsersPerSec(4) during(30 minutes)
  ).protocols(httpProtocol))
}