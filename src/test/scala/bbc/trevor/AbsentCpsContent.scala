package bbc.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AbsentCpsContent extends Simulation {
   
    val httpProtocol = http
        .baseURL("http://newsapps-trevor-producer.int.cloud.bbc.co.uk")

    val r = scala.util.Random
    val scn = scenario("Absent CPS content")
        .exec(http("Absent CPS content")
        .get("/content/cps/news/attack/load-attack-" + r.nextInt(Integer.MAX_VALUE))
        .check(status.is(200))
    ) 

    setUp(scn.inject(
        rampUsersPerSec(10) to(250) during(2 minutes),
        constantUsersPerSec(250) during(18 minutes)
    ).protocols(httpProtocol))

}
