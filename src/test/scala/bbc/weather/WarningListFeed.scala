package bbc.weather

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class WeatherListFeed extends Simulation {

    val httpProtocol = http
        .baseURL("http://weather-warnings-publish.test.cloud.bbc.co.uk")

    val scn = scenario("Weather List Feed")
        .exec(http("Weather List Feed")
        .get("/feeds/en/warnings-list.json")
        .check(status.is(200))
    ) 

    setUp(scn.inject(
        rampUsersPerSec(10) to(300) during(10 minutes),
        constantUsersPerSec(300) during(10 minutes),
        rampUsersPerSec(300) to(750) during(10 minutes),
        constantUsersPerSec(750) during(10 minutes)
    ).protocols(httpProtocol))

}
