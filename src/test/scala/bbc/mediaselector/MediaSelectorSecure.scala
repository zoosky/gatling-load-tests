package bbc.mediaselector

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

// c3.xlarge with 7.5G mem
// sbt -J-Xmx5G -J-Xms2G
class MediaSelectorSecure extends Simulation {
    
    val httpProtocol = http
        .header("X-IP-Address", "") // https://api.stage.bbc.co.uk/mediaselector/kipps?ip=194.159.80.39
    
    val secure = csv("media-selector/secure.csv").circular

    val scn = scenario("media-selector")
        .feed(secure)
        .exec(http("secure")
        .get("https://ipsecure.stage.bbc.co.uk${secureUrl}")
        .check(status.is(200)))

    setUp(scn.inject(
        // running on 4 instances (200 x 4 == 800) 
        // build docker images!!!!
        rampUsersPerSec(10) to(200) during(10 minutes) 
    ).protocols(httpProtocol))
}
