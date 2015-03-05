package bbc.loadtest.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Resolver extends Simulation {

  val httpProtocol = http
    .baseURL("http://newsapps-trevor-resolver.int.cloud.bbc.co.uk")

  val resolver = csv("trevor/resolver.csv").circular

  val scn = scenario("Resolver")
    .feed(resolver)
    .exec(http("Resolver")
    .get("${content}")
    .check(status.in(Seq(200, 202))))

  setUp(scn.inject(
    rampUsersPerSec(10) to(400) during(10 minutes)
  ).protocols(httpProtocol))
}
