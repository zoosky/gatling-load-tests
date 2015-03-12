package bbc.loadtest.mybbc

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class Newsletter extends Simulation {

  val httpProtocol = http
    .baseURL("https://ssl.stage.bbc.co.uk")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .acceptLanguageHeader("en-GB,en;q=0.5")
    .connection("keep-alive")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:35.0) Gecko/20100101 Firefox/35.0")

  val newsletterPage = http("newsletterPage")
    .get("/newsletters/thenolanshow")
    .check(status.is(200),
      substring("To subscribe you need to be 13 or over and have a BBC iD"))

  val signInGet = http("signInGet") 
    .get("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
    .check(status.is(200))

  val signInPost = http("signInPost") 
    .post("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
    .formParam("unique", "adrian@loadtest1.com") 
    .formParam("password", "loadtest")
    .formParam("bbcid_submit_button", "Sign in")
    .check(status.is(200),
      substring("Please confirm your age"))

  val subscribe = http("subscribe")
    .post("https://bbcsignups.external.bbc.co.uk/inxmail4/subscription/servlet")
    .formParam("INXMAIL_HTTP_REDIRECT", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/subscribe?ptrt=http://www.stage.bbc.co.uk")
    .formParam("INXMAIL_HTTP_REDIRECT_ERROR", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/error?ptrt=http://www.stage.bbc.co.uk")
    .formParam("INXMAIL_SUBSCRIPTION", "the nolan show")
    .formParam("tandc_06", "true")
    .formParam("email", "adrian@loadtest1.com")
    .formParam("u13-confirmation", "0")
    .check(status.is(200),
      substring("Check your inbox to confirm your email"))

  val registerGet = http("registerGet") 
    .get("https://ssl.stage.bbc.co.uk/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
    .check(status.is(200),
      substring("Register"))

  val registerPost = http("registerPost") 
    .post("https://ssl.stage.bbc.co.uk/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
    .formParam("email", session => s"random@${new Random().nextInt(Int.MaxValue)}.com")
    .formParam("confirmpassword", "loadtest")
    .formParam("confirmpassword_confirm", "loadtest")
    .formParam("bbcid_submit_button", "Register")
    .check(status.is(200),
      substring("registration is complete"))

  // journey b
  val signInSubscribe = scenario("signInSubscribe")
    .exec(newsletterPage)
    .exec(signInGet)
    .exec(signInPost)
    .exec(subscribe)

  // journey c 
  val registerSubscribe = scenario("registerSubscribe")
    .exec(newsletterPage)
    .exec(registerGet)
    .exec(registerPost)
    .exec(subscribe)

  // journey d
  val nonSignedVisitNewsletter = scenario("nonSignedVisitNewsletter")  
    .exec(newsletterPage)

  setUp(
    signInSubscribe.inject(atOnceUsers(2)).protocols(httpProtocol),
    nonSignedVisitNewsletter.inject(atOnceUsers(2)).protocols(httpProtocol),
    registerSubscribe.inject(atOnceUsers(2)).protocols(httpProtocol)
  )
}
