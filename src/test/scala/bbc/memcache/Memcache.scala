package bbc.memcache

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class Memcache extends Simulation {
    val httpProtocol = http
        .baseURL("http://www.stage.bbc.co.uk")
        .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""", """.*\.svg"""), WhiteList())
        .acceptHeader("""text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8""")
        .acceptEncodingHeader("""gzip, deflate, sdch""")
        .acceptLanguageHeader("""en-GB,en;q=0.5""")
        .connection("""keep-alive""")
        .userAgentHeader("""Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:33.0) Gecko/20100101 Firefox/33.0""")

    val hp = scenario("Homepage")
        .group("Homepage") {
            exec(http("domestic").get("/").check(status.is(200)))
            .exec(http("wales").get("/wales/").check(status.is(200)))
            .exec(http("scotland").get("/scotland/").check(status.is(200)))
            .exec(http("northern Ireland").get("/northernireland/").check(status.is(200)))
            .exec(http("cymru").get("/cymru/").check(status.is(200)))
            .exec(http("alba").get("/alba/").check(status.is(200)))
        }

    val food = scenario("Food")
        .group("Food") {
            exec(http("food").get("/food/").check(status.is(200)))
            .exec(http("programmes").get("/food/programmes/").check(status.is(200)))
            .exec(http("recipe: fish curry").get("/food/recipes/madras_fish_curry_of_82254/").check(status.is(200)))
            .exec(http("recipe: japanese").get("/food/recipes/ajitsuke_tamago_japanese_65686/").check(status.is(200)))
            .exec(http("recipe: egg").get("/food/recipes/search?keywords=egg/").check(status.is(200)))
            .exec(http("recipe: pear").get("/food/recipes/search?keywords=pear/").check(status.is(200)))
            .exec(http("recipe: chicken").get("/food/recipes/search?keywords=chicken/").check(status.is(200)))
            .exec(http("beef").get("/food/beef/").check(status.is(200)))
            .exec(http("ingredients").get("/food/ingredients/").check(status.is(200)))
            .exec(http("chefs").get("/food/chefs/").check(status.is(200)))
            .exec(http("recipes").get("/food/recipes/").check(status.is(200)))
        }

    val authorisationRequest = "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"

    val bbcId = scenario("BBC ID")
        .group("BBC ID") { 
            exec(http("sign-in page")
            .get("https://ssl.stage.bbc.co.uk/oauth/authorize?response_type=token&client_id=iplayer-ios&redirect_uri=http://www.bbc.co.uk/iPlayer&scope=plays.any.r")
            .check(status.is(200)))
            .pause(2)
            .exec(http("submit sign-in")
            .post("https://ssl.stage.bbc.co.uk/id/signin?ptrt=https%3A%2F%2Fssl.stage.bbc.co.uk%2Foauth%2Fauthorize%3Fredirect_uri%3Dhttp%253A%252F%252Fwww.bbc.co.uk%252FiPlayer%26scope%3Dplays.any.r%26response_type%3Dtoken%26client_id%3Diplayer-ios&mode=embedded")
            .formParam("unique", "ADRIAN@LOADTEST1.com")
            .formParam("password", "loadtest")
            .formParam("bbcid_submit_button", "Sign in")
            .check(status.is(200)))
            .pause(2)
            .exec(http("oauth")
            .get("https://ssl.stage.bbc.co.uk/oauth/authorize?redirect_uri=http%3A%2F%2Fwww.bbc.co.uk%2FiPlayer&scope=plays.any.r&response_type=token&client_id=iplayer-ios")
            .check(status.is(200)))
            .pause(2)
            .exec(http("authorise")
            .post("https://ssl.stage.bbc.co.uk/oauth/authorize")
            .formParam("authorizationRequest", authorisationRequest)
            .formParam("authenticationToken", "1yWbCIkKoxKsy1QHbWOTN8+QJx1HZr6FWcMD/y6T1qM5YR6YMhH2PDhu4m1iXONG")
            .formParam("user_oauth_approval", "true")
            .formParam("authorize", "Allow")        
            .formParam("_csrf_signature", "KLmFyNCNWUzgQg2Ml+w9icez25386kkYm70adA3CdJ+MbABYDcBLiAKFyH43kHNB5uUlVcqwrFzCslv01zw+p5vcsDafPgnMb0F/DVLLlOVvkU+J215SbgnVQqkEy1hA")
            .check(status.is(200)))
            .pause(2)
      }

    setUp(
        hp.inject(constantUsersPerSec(50) during(20 minutes)),
        food.inject(constantUsersPerSec(10) during(20 minutes)),
        bbcId.inject(constantUsersPerSec(10) during(20 minutes))
      ).protocols(httpProtocol)
}
