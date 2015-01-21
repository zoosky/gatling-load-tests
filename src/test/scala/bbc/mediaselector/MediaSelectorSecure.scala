package bbc.mediaselector

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MediaSelectorSecure extends Simulation {

    val httpProtocol = http
        .header("X-IP-Address", "")

    val secure = csv("media-selector/secure.csv").circular

    val scn = scenario("media-selector")
        .feed(secure)
        .exec(http("secure")
        .get("https://ipsecure.stage.bbc.co.uk${secureUrl}")
        .check(status.is(200)))

    setUp(scn.inject(
        rampUsersPerSec(10) to(750) during(10 minutes) 
    ).protocols(httpProtocol))
}
