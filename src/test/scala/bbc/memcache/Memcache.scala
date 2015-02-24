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
    
    
    val bbcId = scenario("BBC ID")
      .group("BBC ID") { 
        exec(http("sign-in page")
          .get("https://ssl.stage.bbc.co.uk/id/signin")
          .check(status.is(200)))
          .pause(5)
          .exec(http("submit sign-in")
          .post("https://ssl.stage.bbc.co.uk/id/signin")
          .formParam("unique", "ADRIAN@LOADTEST1.com")
          .formParam("password", "loadtest")
          .formParam("bbcid_submit_button", "Sign in")
          .check(
            status.is(200),
            regex("Your account").exists)
        )
      }

    setUp(
      hp.inject(constantUsersPerSec(50) during(20 minutes)),
      food.inject(constantUsersPerSec(10) during(20 minutes)),
      bbcId.inject(constantUsersPerSec(10) during(20 minutes))
    ).protocols(httpProtocol)
}
