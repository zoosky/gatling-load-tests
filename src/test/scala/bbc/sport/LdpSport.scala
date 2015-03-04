package bbc.loadtest.sport

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import bbc.loadtest.utils.SystemProps

class LdpSport extends Simulation with SystemProps {

  val apiKey = "aszYdyTIisgk9XEwAg9rlnSrjAlDhkWG"
  val palUrls = csv("sport/pal-client.txt").circular 
  val sportDataUrls = csv("sport/sport-data.txt").circular 

  val httpProtocol = http
    .baseURL(s"https://api.$env.bbc.co.uk/ldp-sport")
    .acceptHeader("application/json-ld")
    .disableCaching
    
  val palClient = scenario("PAL client")
    .feed(palUrls) 
    .exec(http("pal client")
    .get("${palUrl}api_key=" + apiKey)
    .check(status.is(200)))

  val cpsNavbuilder = scenario("CPS Navbuilder")
    .exec(http("nav builder")
    .get(s"/nav?api_key=$apiKey")
    .check(status.is(200)))

  val sportsData = scenario("Sports Data")
    .feed(sportDataUrls) 
    .exec(http("sports data")
    .get("${palUrl}api_key=" + apiKey)
    .check(status.is(200)))
    
  setUp(
    palClient.inject(
      atOnceUsers(11)
    ),
    cpsNavbuilder.inject(
      atOnceUsers(1)
    )
  ).protocols(httpProtocol)
}
