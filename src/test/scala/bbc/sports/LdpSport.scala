package bbc.sports

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LdpSport extends Simulation {

  val env = System.getProperty("env")

  val httpProtocol = http
    .baseURL(s"https://api.$env.bbc.co.uk/ldp-sport")
    .disableCaching
    
  val apiKey = "aszYdyTIisgk9XEwAg9rlnSrjAlDhkWG"

  val scn = scenario("Sport")
    .group("Football") {
      exec(http("digests").get(s"/football/competitions/premier-league/teams/digests?api_key=$apiKey").check(status.is(200)))
      .exec(http("premier league teams").get(s"/football/competitions/premier-league/teams?api_key=$apiKey").check(status.is(200)))
      .exec(http("sports data").get(s"/football/teams/urn:sports-data:TFBB60?api_key=$apiKey").check(status.is(200)))
      .exec(http("man united").get(s"/football/team/manchester-united?api_key=$apiKey").check(status.is(200)))
      .exec(http("venues").get(s"/football/venues?api_key=$apiKey").check(status.is(200)))
      .exec(http("teams").get(s"/football/teams?api_key=$apiKey").check(status.is(200)))
      .exec(http("competitions").get(s"/football/competitions/premier-league,championship?api_key=$apiKey").check(status.is(200)))
      .exec(http("teams paginated").get(s"/football/teams/paginated?pageSize=10&page=1&api_key=$apiKey").check(status.is(200)))
    }

    .exec(http("boxing").get(s"/boxing/competition/commonwealth-games/stage/mens-heavy-91kg?season=2014&api_key=$apiKey").check(status.is(200)))
    .exec(http("navigation").get(s"/nav?api_key=$apiKey").check(status.is(200)))
    .exec(http("thing").get(s"/thing/premier-league?api_key=$apiKey").check(status.is(200)))
    .exec(http("formula1").get(s"/formula1/people?api_key=$apiKey").check(status.is(200)))
    .exec(http("discipline").get(s"/discipline/football?api_key=$apiKey").check(status.is(200)))
    .exec(http("winter-olympics").get(s"/winter-olympics/disciplines?api_key==$apiKey").check(status.is(200)))
         
  setUp(scn.inject(
    atOnceUsers(1)
  ).protocols(httpProtocol))
}
