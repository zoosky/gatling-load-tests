import java.util.Date
import java.text.SimpleDateFormat

  
object MetricPrinter {
  
  def dateFormat(unixTime: Long) = {
    val simpleDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss")
    val now = new Date(unixTime * 1000L)
    simpleDateFormat.format(now)
  }

  def main(args: Array[String]) {
    for { 
      ln <- io.Source.stdin.getLines 
      if ln.contains("allRequests.all.count")
        countList = ("""(\d+)""".r findAllIn ln).toList
    } println("Time: " + dateFormat(countList(1).toLong) + ", RPS: " + countList(0))
  }
}
