package bbc.weather

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class WeatherWarningApi extends Simulation {

    // .baseURLs uses a Randam generator which is uniformly distributed. 
    // 50% of hits will be http and 50% https
    val httpProtocol = http
        .baseURLs("https://weather-warnings-publish.test.cloud.bbc.co.uk", "http://weather-warnings-publish.test.cloud.bbc.co.uk")

    val gssIdFeeder = csv("weather/weather-stage-gss-ids.csv").circular

    val scn = scenario("Weather Warning API")
        .feed(gssIdFeeder)
        .exec(http("Weather Warning API")
        .get("/feeds/en/${gss_id}/warnings.json?timezone=Europe/London")
        .check(status.is(200))
    ) 

    setUp(scn.inject(
         atOnceUsers(4)
    ).protocols(httpProtocol))

}
