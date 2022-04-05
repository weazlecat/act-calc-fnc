package act.calc.fnc.data

import org.scalatest.funsuite.AnyFunSuite

class StockRecordTest extends AnyFunSuite {

  test("StockRecord - 01") {
    val s = StockRecord.swapCol(0)
    assert(s=="P000")
  }

  test("StockRecord - 02") {
    val s = StockRecord.swapCol(10)
    assert(s=="P010")
  }

}
