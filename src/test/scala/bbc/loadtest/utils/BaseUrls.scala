package bbc.loadtest.utils

trait BaseUrls {
  private val prop = Option(System.getProperty("env")).getOrElse("a valid environment has not been entered")

  val env = prop.toUpperCase match {
    case "TEST" => "http://data.test.bbc.co.uk"
    case "LIVE" => "http://data.bbc.co.uk"
    case e      => throw new IllegalArgumentException(e)
  }
}
