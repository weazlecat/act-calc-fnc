package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.fnc.api.FncEnvironmentTestHelper
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory

class CalculatorTest extends AnyFunSuite {

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("Calculator") {
    for (item <- CalculatorTestCase.testRecordList) {
      logger.debug("Calculator - ID[%s]".format(item.ID))
      val calc = new Calculator(
        CalculatorTestCase.testStockData,
        Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
        ShortDate(item.DATE_OF_STOCK_FROM),
        ShortDate(item.DATE_OF_STOCK_TO),
        FncEnvironmentTestHelper.testFncConfig.calc
      )
      val resultClc: CalcRecord = calc.projection(ShortDate(item.DATE_OF_PROJECTION_TO), item.MATURITY, item.AVERAGE).select(ShortDate(item.DATE))
      val resultOrg: CalcRecord = CalculatorTestCase.calcRecord(item)
      assert(resultClc == resultOrg)
    }
  }
}



