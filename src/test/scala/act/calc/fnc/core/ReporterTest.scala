package act.calc.fnc.core

import act.calc.com.Checksum
import act.calc.com.shortdate.ShortDate
import act.calc.fnc.api.FncEnvironmentTestHelper
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory

class ReporterTest extends AnyFunSuite {

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("Reporter") {

    for (item <- ReporterTestCase.testRecordList) {
      logger.debug("Reporter - ID[%s]".format(item.ID))
      val calc = new Calculator(
        ReporterTestCase.testStockData,
        Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
        ShortDate(item.DATE_OF_STOCK_FROM),
        ShortDate(item.DATE_OF_STOCK_TO),
        FncEnvironmentTestHelper.testFncConfig.calc
      )
      val repo = new Reporter(calc, FncEnvironmentTestHelper.testFncConfig.repo)
      repo.report(ShortDate(item.DATE_OF_REPORT), ShortDate(item.DATE_OF_PROJECTION_TO), item.MATURITY, item.AVERAGE_A, item.AVERAGE_B, ReporterTestCase.fileIdClc(item.ID).name)
      val checksumClc = new Checksum(ReporterTestCase.fileIdClc(item.ID))
      val checksumOrg = new Checksum(ReporterTestCase.fileIdOrg(item.ID))
      logger.debug("Checksum CLC[%s]".format(checksumClc))
      logger.debug("Checksum ORG[%s]".format(checksumOrg))
      assert(checksumClc==checksumOrg)
    }
  }
}
