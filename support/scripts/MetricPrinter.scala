import java.util.Date
import java.text.SimpleDateFormat

object MetricPrinter {
  
  def dateFormat(unixTime: Long) = {
    val simpleDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss")
    val now = new Date(unixTime * 1000L)
    simpleDateFormat.format(now)
  }

  def printMetrics(data: String): Unit = {
    val rpsAndDate = listRpsAndDate(data)
    if (!rpsAndDate.isEmpty)    
      println("Date Time: " + dateFormat(rpsAndDate(1).toLong) + ", RPS: " + rpsAndDate(0))

    val percentileAndDate = listPercentileAndDate(data)
    if (!percentileAndDate.isEmpty)    
      println("Date Time: " + dateFormat(percentileAndDate(3).toLong) + ", 95%: " + percentileAndDate(2))
  }

  def listRpsAndDate(data: String): List[String] = {
    if (data.contains("count"))
      ("""(\d+)""".r findAllIn data).toList
    else 
      List()
  }

  def listPercentileAndDate(data: String): List[String] = {
    if (data.contains("percentiles95"))
      ("""(\d+)""".r findAllIn data).toList
    else 
      List()
  }

  def main(args: Array[String]) {
    for { 
      ln <- io.Source.stdin.getLines 
      if ln.contains("allRequests.all")
    } printMetrics(ln)
  }
}
