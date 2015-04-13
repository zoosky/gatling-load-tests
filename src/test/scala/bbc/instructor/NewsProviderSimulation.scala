package instructor

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class NewsProviderSimulation extends Simulation {

  val httpProtocol = http
    .baseURL("http://open.stage.bbc.co.uk/redbutton/newsprovider")
    .acceptHeader("application/json")

  val random = new scala.util.Random

  val textIndexChain =
    exec(
      http("${category} index")
        .get("/index/category/${category}/collection.json?platform=html")
        .check(regex("""(/story\.json\?assetUri=.+?)"""").findAll.saveAs("stories")))

    .exec( session => {
      val stories = session("stories").as[List[String]]
      val storyUrl = stories.apply(random.nextInt(stories.size))
      val pattern = "assetUri=(.+?)&".r
      val Some(m) = pattern findFirstMatchIn storyUrl
      val storyId = m.group(1)
      session.set("storyUrl", storyUrl).set("storyId", storyId)
    })

    .exec(
      http("text story ${storyId}")
        .get("${storyUrl}")
        .check(regex("errorItem").notExists))

  val videoIndexChain =
   exec(
      http("${category} index")
        .get("/index/category/${category}/collection.json?platform=html")
        .check(regex("missingContent").notExists))

  val scn = scenario("NewsProviderSimulation")
    .exec(http("Default index")
    .get("/default.json?platform=html"))

    .exec(http("Categories")
    .get("/categories.json?platform=html")
    .check(regex("""/newsprovider/index/category/(\w+)/""").findAll.saveAs("categories")))
    .exec( session => {
      val categories = session("categories").as[List[String]]
      val randomCategory = categories.apply(random.nextInt(categories.size))
      session.set("category", randomCategory)})
    .doIfEqualsOrElse("${category}", "videos") {
      videoIndexChain
    } {
      textIndexChain
    }

  setUp(scn.inject(
    // typical peak usage
    constantUsersPerSec(1) during(2 minutes),
    nothingFor(30 seconds),
    // aspirational peak usage
    constantUsersPerSec(7) during(5 minutes),
    nothingFor(30 seconds),
    // find breaking point
    rampUsersPerSec(7) to(100) during(5 minutes))
  ).protocols(httpProtocol)
}
