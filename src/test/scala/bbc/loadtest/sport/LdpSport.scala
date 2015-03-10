package bbc.loadtest.sport

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import bbc.loadtest.utils.BaseUrls

class LdpSport extends Simulation with BaseUrls {

  val palUrls = csv("sport/pal-client.txt").random 
  val sportDataUrls = csv("sport/sport-data.txt").random

  val httpProtocol = http
    .baseURL(env)
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
    
    .exec(http("Sports Data") 
    .get("${sportDataUrl}")
    .header("Accept", "application/xml")
    .check(status.in(200, 201, 404)))

  setUp(ldpSport.inject(
    constantUsersPerSec(10) during(5 minutes)
  ).protocols(httpProtocol))
}
