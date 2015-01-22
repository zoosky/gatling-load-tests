import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Register extends Simulation {

	val httpProtocol = http
		.baseURL("https://ssl.stage.bbc.co.uk")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.8,en;q=0.6")
		.connection("keep-alive")
		.contentTypeHeader("application/x-www-form-urlencoded")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")

	val getHeader = Map("Accept-Encoding" -> "gzip, deflate, sdch")
	val postHeader = Map("Origin" -> "https://ssl.stage.bbc.co.uk")

	val scn = scenario("Register")
      .exec(http("get")
			.get("/id/register?mode=embedded")
      .check(status.is(200)) 
			.headers(getHeader))
      .exec(http("post")
			.post("/id/register")
			.headers(postHeader)
			.formParam("email", "super@aidy2.com")
			.formParam("confirmpassword", "superaidy")
			.formParam("confirmpassword_confirm", "superaidy")
			.formParam("bbcid_submit_button", "Register")
      .check(status.is(200)) 
      .check(substring("Your registration is complete")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
