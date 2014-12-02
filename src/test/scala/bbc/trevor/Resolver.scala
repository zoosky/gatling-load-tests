package bbc.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Resolver extends Simulation {

    val httpProtocol = http
        .baseURL("http://newsapps-trevor-producer.int.cloud.bbc.co.uk")
        .disableFollowRedirect

    val resolver = csv("trevor/resolver.csv").circular

    val scn = scenario("Resolver")
        .feed(resolver)
        .exec(http("Resolver")
        .get("${content}")
    ) 

    setUp(scn.inject(
        rampUsersPerSec(10) to(250) during(2 minutes),
        constantUsersPerSec(250) during(18 minutes)
    ).protocols(httpProtocol))

}
