package bbc.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ProducerCpsEventRenderer extends Simulation {

    val httpProtocol = http
        .baseURL("https://newsapps-trevor-dev-utils.int.cloud.bbc.co.uk")

    val renderer = csv("trevor/producer-cps-event-renderer.csv").circular

    val scn = scenario("Producer CPS Event Renderer")
        .feed(renderer)
        .exec(http("Producer CPS Event Renderer")
        .get("${content}")
        .check(status.is(200))
    ) 

    setUp(scn.inject(
        rampUsersPerSec(1) to(10) during(2 minutes),
        constantUsersPerSec(10) during(18 minutes)
    ).protocols(httpProtocol))

}
