package bbc.loadtest.sport

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ThingsToDo extends Simulation { 
  val httpProtocol = http
    .baseURLs("http://m.stage.bbc.co.uk", "http://m.stage.bbc.com")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
    .acceptEncodingHeader("""gzip, deflate""")
    .acceptLanguageHeader("""en-gb,en;q=0.5""")
    .connection("""keep-alive""")
    .userAgentHeader("""Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30""")

  val cricketFeeder = csv("sport/cricket-teams.csv").circular
  val footballFeeder = csv("sport/football-teams.csv").circular 
  val indexFeeder = csv("sport/indexes.csv").circular 
  val rugbyLeagueFeeder = csv("sport/rugby-league-teams.csv").circular 
  val rugbyUnionFeeder = csv("sport/rugby-union-teams.csv").circular 

  val scn = scenario("Things To Do")
    .feed(cricketFeeder)
    .exec(http("Cricket")
    .get("${cricketTeam}")
    .check(status.is(200)))
    
    .feed(footballFeeder)
    .exec(http("Football")
    .get("${footballTeam}")
    .check(status.is(200)))
    
    .feed(indexFeeder)
    .exec(http("Indexes")
    .get("${index}")
    .check(status.is(200)))
    
    .feed(rugbyLeagueFeeder)
    .exec(http("Rugby League")
    .get("${rugbyLeagueTeam}")
    .check(status.is(200)))

    .feed(rugbyUnionFeeder)
    .exec(http("Rugby Union")
    .get("${rugbyUnionTeam}")
    .check(status.is(200)))
      
  setUp(scn.inject(
    rampUsersPerSec(10) to(250) during(30 minutes)
  ).protocols(httpProtocol))
}
