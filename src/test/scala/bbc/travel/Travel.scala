package bbc.travel

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Travel extends Simulation {

    val httpProtocol = http
        .baseURL("http://www.stage.bbc.co.uk")
        .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.svg""", """.*\.png"""), WhiteList())
        .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
        .acceptEncodingHeader("""gzip, deflate""")
        .acceptLanguageHeader("""en-gb,en;q=0.5""")
        .connection("""keep-alive""")

    val geonameIdFeeder = csv("travel/data_001_geonames.csv").circular
    val tileFeeder = csv("travel/data_002_tiles.csv").circular

    val scn = scenario("Travel")
        .exec(http("Homepage").get("/travel").check(status.is(200)))
        .feed(geonameIdFeeder)
        .exec(http("Road Incidents").get("/travel/${geoname_id}/incidents/road").check(status.is(200)))
        .feed(tileFeeder)
        .exec(http("Road Map Tiles").get("/travel/map-cell/${tile}").check(status.is(200)))
        .exec(http("Rail Incidents").get("/travel/${geoname_id}/incidents/rail").check(status.is(200)))
        .exec(http("Planned Rail Incidents").get("/travel/${/geoname_id}/incidents/rail/planned").check(status.is(200)))
        .exec(http("Light Rail Incidents").get("/travel/${geoname_id}/incidents/light-rail").check(status.is(200)))
        
    setUp(scn.inject(
        // This load injection is just for testing the test
        atOnceUsers(5)
    ).protocols(httpProtocol))
}
