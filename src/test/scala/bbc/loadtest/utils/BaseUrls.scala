package bbc.loadtest.utils

trait BaseUrls {
  private val prop = System.getProperty("env") 

  val env = prop.toUpperCase  match {
    case "TEST" => "http://data.test.bbc.co.uk"
    case "LIVE" => "http://data.bbc.co.uk"
    case _      => throw new IllegalArgumentException("a valid environment has not been entered")
  }
}
