package bbc.loadtest.sport

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bbc.loadtest.utils.SystemProps

class LdpSport extends Simulation with SystemProps {

  val palUrls = csv("sport/pal-client.txt").random 
  val sportDataUrls = csv("sport/sport-data.txt").random

  val httpProtocol = http
    .baseURL(s"http://data.$env.bbc.co.uk")
    .acceptHeader("application/json-ld")
    .disableCaching

  val palClient = http("Pal Client").get("${palUrl}").check(status.is(200))
  val cpsNavBuilder = http("Nav Builder").get("https://api.test.bbc.co.uk/ldp-sport/nav?api_key=aszYdyTIisgk9XEwAg9rlnSrjAlDhkWG").check(status.is(200))
  val sportData = http("Sports Data").get(s"http://data.$env.bbc.co.uk" + "${sportDataUrl}").header("Accept", "application/xml").check(status.is(200))

  val ldpSport = scenario("ldpSport")
    .feed(palUrls)
    .exec(palClient)
    .exec(cpsNavBuilder)
    .feed(sportDataUrls)
    .exec(sportData)

  setUp(ldpSport.inject(
    // constantUsersPerSec(12) during(10 seconds)
    atOnceUsers(2)
  ).protocols(httpProtocol))
}
