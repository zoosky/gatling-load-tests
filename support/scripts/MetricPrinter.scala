import java.util.Date
import java.text.SimpleDateFormat

object MetricPrinter {
  
  def dateFormat(unixTime: Long) = {
    val simpleDateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm:ss")
    val now = new Date(unixTime * 1000L)
    simpleDateFormat.format(now)
  }

  def rpsAndDate(data: String) = {
    val countValues =
      if (data.contains("count"))
        ("""(\d+)""".r findAllIn data).toList
      else 
        List()

    if (!countValues.isEmpty)
      println("Date Time: " + dateFormat(countValues(1).toLong) + ", RPS: " + countValues(0))
  }

  def main(args: Array[String]) {
    for { 
      ln <- io.Source.stdin.getLines 
      if ln.contains("allRequests.all")
    } rpsAndDate(ln)
  }
}
