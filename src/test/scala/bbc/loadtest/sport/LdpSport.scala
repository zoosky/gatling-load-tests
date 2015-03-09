package bbc.loadtest.sport

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bbc.loadtest.utils.SystemProps

class LdpSport extends Simulation with SystemProps {

  val palUrls = csv("sport/pal-client.txt").random 
  val sportDataUrls = csv("sport/sport-data.txt").random

  val httpProtocol = http
    .baseURL(s"http://data$env.bbc.co.uk")
    .acceptHeader("application/json-ld")
    .disableCaching

  val palClient = http("Pal Client").get("${palUrl}").check(status.in(Seq(200, 201, 404)))
  val cpsNavBuilder = http("Nav Builder").get(s"https://api$env.bbc.co.uk/ldp-sport/nav?api_key=aszYdyTIisgk9XEwAg9rlnSrjAlDhkWG").check(status.in(Seq(200, 201, 404)))
  val sportData = http("Sports Data").get("${sportDataUrl}").header("Accept", "application/xml").check(status.in(Seq(200, 201, 404)))

  val ldpSport = scenario("ldpSport")
    .feed(palUrls)
    .exec(palClient).pause(1)
    .exec(cpsNavBuilder).pause(1)
    .feed(sportDataUrls)
    .exec(sportData).pause(1)

  setUp(ldpSport.inject(
    // atOnceUsers(2)
    rampUsersPerSec(1) to 100 during(5 seconds),    //1
    constantUsersPerSec(100) during(10 minutes),    //2
    rampUsersPerSec(100) to 1000 during(20 minutes), //3
    constantUsersPerSec(1000) during(30 minutes)    //4
  ).protocols(httpProtocol))
}
