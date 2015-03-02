package bbcid

import scala.concurrent.duration._
import scala.util.Random
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Register extends Simulation {

	val httpProtocol = http
		.baseURL("https://ssl.bbc.co.uk")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.5")
		.connection("keep-alive")
		.contentTypeHeader("application/x-www-form-urlencoded")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:35.0) Gecko/20100101 Firefox/35.0")

	val scn = scenario("Register")
    .exec(http("register")
    .get("/id/register"))
    .exec(http("post register")
    .post("/id/register")
    .formParam("email", session => s"aidy@${new Random().nextInt(2147483647)}.com")
    .formParam("confirmpassword", "loadtest")
    .formParam("confirmpassword_confirm", "loadtest")
    .check(substring("registration is complete")))

    setUp(scn.inject(
      rampUsersPerSec(10) to(150) during(5 minutes) 
    ).protocols(httpProtocol))
}
