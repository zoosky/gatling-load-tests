object MetricPrinter {

  def main(args: Array[String]) {
    for { 
      ln <- io.Source.stdin.getLines 
      if ln.contains("allRequests.all.count")
        countList = ("""(\d+)""".r findAllIn ln).toList
    } println("Time: " + countList(1) + " RPS: " + countList(0))
    
  }
}
