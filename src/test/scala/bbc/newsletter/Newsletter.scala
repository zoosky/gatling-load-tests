package bbc.newsletter

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random
import java.util.concurrent.atomic._

class Newsletter extends Simulation {

  val httpProtocol = http
    .baseURL("https://ssl.stage.bbc.co.uk")
    .acceptCharsetHeader("ISO-8859-1,utf-8;q=0.7,*;q=0.7")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptLanguageHeader("en-GB,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36")

  val value = new AtomicInteger(1)
  // journey A
  val signedInUserSubscribe = scenario("signedInUserSubscribe")
    // populate cookieJar
    .feed(CookieJarHelper.cookieJarFeeder)
    
    .exec(http("jumpToAgeConfirm").get("/newsletters/thenolanshow").check(substring("Please confirm your age")))
  
    .exec(http("signedInSubscribe")
      .post("https://bbcsignups.external.bbc.co.uk/inxmail4/subscription/servlet")
        .formParam("INXMAIL_HTTP_REDIRECT", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/subscribe?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_HTTP_REDIRECT_ERROR", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/error?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_SUBSCRIPTION", "the nolan show")
        .formParam("tandc_06", "true")
        .formParam("email", session => s"adrian@loadtest${value.getAndIncrement}.com")
        .formParam("u13-confirmation", "0")
          .check(substring("Check your inbox to confirm your email")))

  // journey B
  val signInSubscribe = scenario("signInSubscribe")
    .exec(http("newsLetterPage")
      .get("/newsletters/thenolanshow")
        .check(substring("To subscribe you need to be 13 or over")))
    
    .exec(http("signInGet") 
      .get("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow"))

    .exec(http("signInPost") 
      .post("/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
        .formParam("unique", "loadtest@loadtest.com") 
        .formParam("password", "loadtest")
        .formParam("bbcid_submit_button", "Sign in")
          .check(substring("Please confirm your age")))

    .exec(http("signInSubscribe")
      .post("https://bbcsignups.external.bbc.co.uk/inxmail4/subscription/servlet")
        .formParam("INXMAIL_HTTP_REDIRECT", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/subscribe?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_HTTP_REDIRECT_ERROR", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/error?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_SUBSCRIPTION", "the nolan show")
        .formParam("tandc_06", "true")
        .formParam("email", "loadtest@loadtest.com")
        .formParam("u13-confirmation", "0")
          .check(substring("Check your inbox to confirm your email")))

  // Journey C
  val registerSubscribe = scenario("registerSubscribe")
  .exec(_.set("randomDomain", s"random@${new Random().nextInt(Int.MaxValue)}.com"))
    .exec(http("newsLetterPage")
      .get("/newsletters/thenolanshow")
        .check(substring("To subscribe you need to be 13 or over")))

    .exec(http("registerGet") 
      .get("/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
        .check(substring("Register")))
    
    .exec(http("registerPost") 
      .post("/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Fnewsletters%2Fthenolanshow")
        .formParam("email", "${randomDomain}")
        .formParam("confirmpassword", "loadtest")
        .formParam("confirmpassword_confirm", "loadtest")
        .formParam("bbcid_submit_button", "Register")
          .check(substring("registration is complete")))
    
    .exec(http("registerSubscribe")
      .post("https://bbcsignups.external.bbc.co.uk/inxmail4/subscription/servlet")
        .formParam("INXMAIL_HTTP_REDIRECT", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/subscribe?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_HTTP_REDIRECT_ERROR", "http://www.stage.bbc.co.uk/newsletters/thenolanshow/error?ptrt=http://www.stage.bbc.co.uk")
        .formParam("INXMAIL_SUBSCRIPTION", "the nolan show")
        .formParam("tandc_06", "true")
        .formParam("email", "${randomDomain}")
        .formParam("u13-confirmation", "0")
          .check(substring("Check your inbox to confirm your email")))

    //journey D
    val nonSignInUserVisitsPage = scenario("nonSignInUserVisitsPage")
      .exec(http("newsLetterPage")
        .get("/newsletters/thenolanshow")
          .check(substring("To subscribe you need to be 13 or over")))

  setUp(
    signedInUserSubscribe.inject(atOnceUsers(1)),
    signInSubscribe.inject(atOnceUsers(1)),
    registerSubscribe.inject(atOnceUsers(1)),
    nonSignInUserVisitsPage.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
