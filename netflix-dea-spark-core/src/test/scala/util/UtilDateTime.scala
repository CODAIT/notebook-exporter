/**
  * Created by rsharma on 2/16/16.
  */
import dea.util._
import org.junit.Test

class UtilDateTime {

  @Test
  def DateTime_lastDayOfMonth() {
    assert(DateTime.lastDayOfMonth(20160216)==20160229)
    }
}
