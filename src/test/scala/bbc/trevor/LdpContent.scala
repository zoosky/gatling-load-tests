package bbc.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LdpContent extends Simulation {
   
    val httpProtocol = http
        .baseURL("http://newsapps-trevor-producer.int.cloud.bbc.co.uk")

    val ldpContent = csv("trevor/ldp-content.csv").circular
    
    val scn = scenario("LDP Content")
        .feed(ldpContent)
        .exec(http("LDP Content")
        .get("${content}")
        .check(status.is(200))
    ) 

    setUp(scn.inject(
        rampUsersPerSec(10) to(250) during(2 minutes),
        constantUsersPerSec(250) during(18 minutes)
    ).protocols(httpProtocol))

}
