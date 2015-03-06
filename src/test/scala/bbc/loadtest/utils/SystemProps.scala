package bbc.loadtest.utils

trait SystemProps {
  val prop = System.getProperty("env") 
  val env = if (prop.toUpperCase == "LIVE") "" else "." + prop
}
