package bbc.thingstodo

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ThingsToDo extends Simulation { 

    val httpProtocol = http
        .baseURL("http://m.bbc.co.uk")
        .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
        .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
        .acceptEncodingHeader("""gzip, deflate""")
        .acceptLanguageHeader("""en-gb,en;q=0.5""")
        .connection("""keep-alive""")
        .userAgentHeader("""Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30""")

    val thingsToDoFeeder = csv("thingstodo/[waiting for URLs]").circular

    val scn = scenario("Things To Do")
        .feed(thingsToDoFeeder)
        .exec(http("things to do")
        .get("${sport}")
        .check(status.is(200)))
      
    setUp(scn.inject(
        rampUsersPerSec(10) to(80) during(5 minutes),
        constantUsersPerSec(80) during(15 minutes)
    ).protocols(httpProtocol))
}


