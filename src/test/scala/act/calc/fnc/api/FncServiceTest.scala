package act.calc.fnc.api

import act.calc.com.Checksum
import act.calc.com.shortdate.ShortDate
import act.calc.fnc.core.*
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory

class FncServiceTest extends AnyFunSuite {

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))
  private val fncService = new FncService(FncEnvironmentTestHelper.testFncConfig)

  test("FncService - Calculator") {
    for (item <- CalculatorTestCase.testRecordList) {
      logger.debug("FncService - Calculator - ID[%s]".format(item.ID))
      val calc = fncService.calculator(
        projection = Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
        dateOfStockFrom = ShortDate(item.DATE_OF_STOCK_FROM),
        dateOfStockTo = ShortDate(item.DATE_OF_STOCK_TO)
      )
      val resultOrg: CalcRecord = CalculatorTestCase.calcRecord(item)
      val resultClc: CalcRecord = calc.projection(ShortDate(item.DATE_OF_PROJECTION_TO), item.MATURITY, item.AVERAGE).select(ShortDate(item.DATE))
      assert(resultClc == resultOrg)
    }
  }

  test("FncService - Projector") {
    for (item <- ProjectorTestCase.testRecordList) {
      logger.debug("FncService - Projector - ID[%s]".format(item.ID))
      val proj = fncService.projector(
        projection = Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
        dateOfStockFrom = ShortDate(item.DATE_OF_STOCK_FROM),
        dateOfStockTo = ShortDate(item.DATE_OF_STOCK_TO)
      )
      proj.project(
        ShortDate(item.DATE_OF_REPORT_FROM),
        ShortDate(item.DATE_OF_REPORT_TO),
        item.AVERAGE,
        ProjectorTestCase.fileIdClc(item.ID).name
      )
      val checksumClc = new Checksum(ProjectorTestCase.fileIdClc(item.ID))
      val checksumOrg = new Checksum(ProjectorTestCase.fileIdOrg(item.ID))
      logger.debug("Checksum CLC[%s]".format(checksumClc))
      logger.debug("Checksum ORG[%s]".format(checksumOrg))
      assert(checksumClc==checksumOrg)
    }
  }

  test("FncService - Reporter") {
    for (item <- ReporterTestCase.testRecordList) {
      logger.debug("FncService - Reporter - ID[%s]".format(item.ID))
      val repo = fncService.reporter(
          projection = Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
          dateOfStockFrom = ShortDate(item.DATE_OF_STOCK_FROM),
          dateOfStockTo = ShortDate(item.DATE_OF_STOCK_TO)
        )
      repo.report(
        ShortDate(item.DATE_OF_REPORT),
        ShortDate(item.DATE_OF_PROJECTION_TO),
        item.MATURITY,
        item.AVERAGE_A,
        item.AVERAGE_B,
        ReporterTestCase.fileIdClc(item.ID).name
      )
      val checksumClc = new Checksum(ReporterTestCase.fileIdClc(item.ID))
      val checksumOrg = new Checksum(ReporterTestCase.fileIdOrg(item.ID))
      logger.debug("Checksum CLC[%s]".format(checksumClc))
      logger.debug("Checksum ORG[%s]".format(checksumOrg))
      assert(checksumClc==checksumOrg)
    }
  }
}
