package bbc.bbcid

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

    // printf '%s\n' ADRIAN@LOADTEST{1..10}.com 
    val emailFeeder = csv("bbcid/email-addresses.csv").queue 

    val scn = scenario("Register")
      .feed(emailFeeder)
      .exec(http("get")
      .get("/id/register?mode=embedded")
      .check(status.is(200)) 
      .headers(getHeader))
      .exec(http("post")
      .post("/id/register")
      .headers(postHeader)
      .formParam("email", "${email}")
      .formParam("confirmpassword", "loadtest")
      .formParam("confirmpassword_confirm", "loadtest")
      .formParam("bbcid_submit_button", "Register")
      .check(status.is(200)) 
      .check(substring("Your registration is complete")))

    setUp(scn.inject(
      constantUsersPerSec(25) during(17 minutes) // around 25,000
    ).protocols(httpProtocol))
}
