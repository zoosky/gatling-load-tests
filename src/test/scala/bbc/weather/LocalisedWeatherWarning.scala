package bbc.weather

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class LocalisedWeatherWarning extends Simulation {

    val httpProtocol = http
        .baseURL("https://weather-warnings-publish.test.cloud.bbc.co.uk")

    val gssIdFeeder = csv("weather/weather-stage-gss-ids.csv").circular

    val scn = scenario("Localised Weather Warning")
        .feed(gssIdFeeder)
        .exec(http("Localised Weather Warning")
        .get("/en/${gss_id}/crbwarnings.json?timezone=Europe/London")
        .check(status.is(200))
    ) 

    setUp(scn.inject(
        rampUsersPerSec(10) to(300) during(10 minutes),
        constantUsersPerSec(300) during(10 minutes),
        rampUsersPerSec(300) to(750) during(10 minutes),
        constantUsersPerSec(750) during(10 minutes)
    ).protocols(httpProtocol))

}
