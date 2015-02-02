package bbc.thingstodo

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ThingsToDo extends Simulation { 

    val httpProtocol = http
        .baseURL("https://api.stage.bbc.co.uk")
        .acceptHeader("""application/ld+json""")
        .acceptEncodingHeader("""gzip, deflate""")
        .connection("""keep-alive""")

    val sportFeeder = csv("thingstodo/linked-data-sport-api.txt").circular

    val scn = scenario("LDP Sport")
        .feed(sportFeeder)
        .exec(http("LDP Sport")
        .get("${sport}")
        .check(status.is(200)))
      
    setUp(scn.inject(
        rampUsersPerSec(10) to(160) during(5 minutes),
        constantUsersPerSec(160) during(10 minutes),
        rampUsersPerSec(160) to(500) during(5 minutes),
        constantUsersPerSec(500) during(5 minutes)
    ).protocols(httpProtocol))
}


