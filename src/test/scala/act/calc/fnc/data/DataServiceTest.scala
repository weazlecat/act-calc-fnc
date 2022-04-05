package act.calc.fnc.data

import act.calc.com.shortdate.ShortDate
import act.calc.fnc.api.FncEnvironmentTestHelper
import org.scalatest.funsuite.AnyFunSuite

class DataServiceTest extends AnyFunSuite {

  private val dataPool = new DataService(FncEnvironmentTestHelper.testFncConfig.stock.csv.fileId, FncEnvironmentTestHelper.testFncConfig.stock.csv.delimiter(0)).data
  private val dateFrom = new ShortDate(2010, 7)
  private val dateTo = new ShortDate(2010, 11)
  private val dateSelect = new ShortDate(2009, 10)

  test("DataService - DATA - 04") {
    val d1 = dataPool.select(dateFrom, dateFrom)
    val d2 = d1.select(dateSelect, dateSelect)
    assert(d2.isEmpty)
  }

  test("DataService - DATA - 05") {
    val d1 = dataPool.select(dateFrom, dateFrom)
    assert(d1.first.DATE==dateFrom.toString)
  }

  test("DataService - DATA - 06") {
    val b = dataPool.contains(dateFrom)
    assert(b)
  }

  test("DataService - DATA - 07") {
    val d = dataPool.select(from=dateFrom, to=dateTo)
    val b = !d.contains(dateSelect)
    assert(b)
  }

  test("DataService - DATA - 08") {
    val d = dataPool.select(from=dateFrom, to=dateTo)
    assert(d.count==5)
  }
}

