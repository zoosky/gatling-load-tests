package bbc.loadtest.utils

trait SystemProps {
  val env = System.getProperty("env")
}
