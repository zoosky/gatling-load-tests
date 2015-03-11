package bbc.loadtest.utils

object BaseUrls {
  private val prop = Option(System.getProperty("env")).getOrElse("a valid environment has not been entered")

  val url = prop.toUpperCase match {
    case "TEST" => Map("data" -> "http://data.test.bbc.co.uk")
    case "LIVE" => Map("data" -> "http://data.bbc.co.uk")
    case e      => throw new IllegalArgumentException(e)
  }
}
