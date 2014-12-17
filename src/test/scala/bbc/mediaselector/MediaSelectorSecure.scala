package bbc.mediaselector

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MediaSelectorSecure extends Simulation {

    val httpProtocol = http
        .header("X-IP-Address", "")

    val open = csv("media-selector/secure.csv").circular

    val scn = scenario("media-selector")
        .exec(http("secure")
        .get("https://ipsecure.stage.bbc.co.uk${secureUrl}")
        .check(status.is(200)))

    setUp(scn.inject(
        rampUsersPerSec(10) to(250) during(10 minutes) 
    ).protocols(httpProtocol))
}
