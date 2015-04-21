package bbc.nitro

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LiveBroker extends Simulation {
  
  val httpProtocol = http
    .baseURL("https://api.stage.bbc.co.uk")
    .acceptHeader("application/json")

  val request = csv("nitro/requests.txt").circular  
  
  val scn = scenario("Live Broker")
    .feed(request)
    .exec(http("live broker")
    .get("/live-broker/moments?request=${requestValue}")
    .check(status.is(200)))

  setUp(scn.inject(
    constantUsersPerSec(10) during(60 minutes)
  ).protocols(httpProtocol))
}
