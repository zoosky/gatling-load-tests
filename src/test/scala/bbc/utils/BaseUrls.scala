package bbc.utils

object BaseUrls {
  private val prop = Option(System.getProperty("env")).getOrElse("a valid environment has not been entered")

  val url = prop.toUpperCase match {
    case "TEST" => Map("data" -> "https://sport-linked-data.test.cloud.bbc.co.uk")
    case "LIVE" => Map("data" -> "https://sport-linked-data.cloud.bbc.co.uk")
    case e      => throw new IllegalArgumentException(e)
  }
}
