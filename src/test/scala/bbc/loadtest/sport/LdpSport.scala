package bbc.loadtest.sport

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bbc.loadtest.utils.BaseUrls._

class LdpSport extends Simulation {

  val palUrls = csv("sport/pal-client.txt").random 
  val sportDataUrls = csv("sport/sport-data.txt").random

  val httpProtocol = http
    .baseURL(url("data"))
    .acceptHeader("application/json-ld")
    .disableCaching

  val ldpSport = scenario("ldpSport")
    .feed(palUrls)
    .exec(http("Pal Client")
    .get("${palUrl}")
    .check(status.in(200, 201, 404)))

    .exec(http("CPS Nav Builder")
    .get("/sport-linked-data/nav?api_key=aszYdyTIisgk9XEwAg9rlnSrjAlDhkWG")
    .check(status.in(200, 201, 404)))
    
    .feed(sportDataUrls)
    .exec(http("Sports Data") 
    .get("${sportDataUrl}")
    .header("Accept", "application/xml")
    .check(status.in(200, 201, 404)))

  setUp(ldpSport.inject(
    rampUsersPerSec(11) to 33 during(10 seconds),
    constantUsersPerSec(33) during(10 minutes),
    rampUsersPerSec(33) to 333 during(20 minutes),
    constantUsersPerSec(333) during(30 minutes)
  ).protocols(httpProtocol))
}
