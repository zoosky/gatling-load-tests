package bbc.loadtest.trevor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MostPopular extends Simulation {
   
  val httpProtocol = http
    .baseURL("http://newsapps-trevor-producer.int.cloud.bbc.co.uk")

  val scn = scenario("Access Most Popular")
    .exec(http("Most Popular")
    .get("/content/most_popular/news")
    .check(status.is(200)))
    .exec(http("Trending")
    .get("/content/trending/events")
    .check(status.in(Seq(200, 202)))) 

  setUp(scn.inject(
    rampUsersPerSec(10) to(400) during(10 minutes)
  ).protocols(httpProtocol))
}
