package bbc.mediaselector

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MediaSelectorOpen extends Simulation {

    val httpProtocol = http
        // .header("X-IP-Address", "")

    val open = csv("media-selector/open.csv").circular

    val scn = scenario("media-selector")
        .feed(open)
        .exec(http("open")
        .get("http://open.stage.cwwtf.bbc.co.uk${openUrl}")
        .check(status.is(200)))

    setUp(scn.inject(
        rampUsersPerSec(100) to(750) during(3 minutes) 
    ).protocols(httpProtocol))
}
