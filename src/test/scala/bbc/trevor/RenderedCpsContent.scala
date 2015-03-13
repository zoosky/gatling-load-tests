package bbc.loadtest.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RenderedCpsContent extends Simulation {

  val httpProtocol = http
    .baseURL("http://newsapps-trevor-producer.int.cloud.bbc.co.uk")

  val renderedCpsContent = csv("trevor/rendered-cps-content.csv").circular

  val scn = scenario("Rendered CPS content")
    .feed(renderedCpsContent)
    .exec(http("Rendered CPS content")
    .get("${content}")
    .check(status.is(200))) 

  setUp(scn.inject(
    rampUsersPerSec(10) to(250) during(2 minutes),
    constantUsersPerSec(250) during(18 minutes)
  ).protocols(httpProtocol))
}
