package bbcid

import scala.concurrent.duration._
import scala.util.Random
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Register extends Simulation {

  val httpProtocol = http
    .baseURL("https://ssl.stage.bbc.co.uk")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-GB,en;q=0.5")
    .connection("keep-alive")
    .contentTypeHeader("application/x-www-form-urlencoded")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:35.0) Gecko/20100101 Firefox/35.0")

  val scn = scenario("Register")
    .exec(http("get register")
    .get("/oauth/authorize?response_type=token&client_id=iplayer-ios&redirect_uri=http://www.bbc.co.uk/iPlayer&scope=plays.any.r&action=register"))

    .exec(http("post is old enough")
    .post("/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Foauth%2Fauthorize%3Fredirect_uri%3Dhttp%253A%252F%252Fwww.bbc.co.uk%252FiPlayer%26scope%3Dplays.any.r%26response_type%3Dtoken%26client_id%3Diplayer-ios&mode=embedded")
    .formParam("isageold", "1")
    .formParam("uncollected_isoversixteenundersixteen", "1")
    .formParam("uncollected_username", "")
    .formParam("uncollected_email", "")
    .formParam("uncollected_confirmpassword", "")
    .formParam("uncollected_confirmpassword_confirm", "")
    .formParam("uncollected_dateofbirthundersixteen_day", "")
    .formParam("uncollected_dateofbirthundersixteen_month", "")
    .formParam("uncollected_dateofbirthundersixteen_year", "")
    .formParam("bbcid_submit_button", "Register"))

    .exec(http("post register")
    .post("/id/register?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Foauth%2Fauthorize%3Fredirect_uri%3Dhttp%253A%252F%252Fwww.bbc.co.uk%252FiPlayer%26scope%3Dplays.any.r%26response_type%3Dtoken%26client_id%3Diplayer-ios&mode=embedded")
    .formParam("collected_isageold", "1")
    .formParam("email", session => s"aidy@${new Random().nextInt(Int.MaxValue)}.com")
    .formParam("confirmpassword", "loadtest")
    .formParam("confirmpassword_confirm", "loadtest")
    .formParam("bbcid_submit_button", "Register")
    .check(substring("registration is complete")))

  setUp(scn.inject(
    rampUsersPerSec(10) to(150) during(5 minutes) 
  ).protocols(httpProtocol))
}
