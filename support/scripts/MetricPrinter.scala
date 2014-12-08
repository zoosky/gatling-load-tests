object MetricPrinter {

  def main(args: Array[String]) {
    for { 
      ln <- io.Source.stdin.getLines 
      if ln.contains("allRequests.all.count")
        count = ("""(\d+)""".r findFirstIn ln)
    } println("RPS: " + count.get)
  }
}
