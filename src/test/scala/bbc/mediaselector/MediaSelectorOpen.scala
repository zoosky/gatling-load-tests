package bbc.mediaselector

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

// c3.xlarge with 7.5G mem
// sbt -J-Xmx5G -J-Xms2G
class MediaSelectorOpen extends Simulation {

    val httpProtocol = http
        // .header("X-IP-Address", "") // https://api.stage.bbc.co.uk/mediaselector/kipps?ip=194.159.80.39

    val open = csv("media-selector/open.csv").circular

    val scn = scenario("media-selector")
        .feed(open)
        .exec(http("open")
        .get("http://open.stage.cwwtf.bbc.co.uk${openUrl}")
        .check(status.is(200)))

    setUp(scn.inject(
         // running on 4 instances (200 x 4 == 800) 
         // build docker images!!!!
        rampUsersPerSec(100) to(200) during(10 minutes) 
    ).protocols(httpProtocol))
}
