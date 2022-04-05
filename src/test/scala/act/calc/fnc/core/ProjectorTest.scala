package act.calc.fnc.core

import act.calc.com.Checksum
import act.calc.com.shortdate.ShortDate
import act.calc.fnc.api.FncEnvironmentTestHelper
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory

class ProjectorTest extends AnyFunSuite {

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("Projector") {
    for (item <- ProjectorTestCase.testRecordList) 
      logger.debug("Projector - ID[%s]".format(item.ID))
      val calc = new Calculator(
        ProjectorTestCase.testStockData,
        Calculator.ProjectionAttrib.valueOf(item.PROJECTION),
        ShortDate(item.DATE_OF_STOCK_FROM),
        ShortDate(item.DATE_OF_STOCK_TO),
        FncEnvironmentTestHelper.testFncConfig.calc)
      val proj = new Projector(
        calc,
        FncEnvironmentTestHelper.testFncConfig.proj
      )
      proj.project(
        ShortDate(item.DATE_OF_REPORT_FROM),
        ShortDate(item.DATE_OF_REPORT_TO),
        item.AVERAGE,
        ProjectorTestCase.fileIdClc(item.ID).name
      )
      
      val checksumClc = new Checksum(ProjectorTestCase.fileIdClc(item.ID))
      val checksumOrg = new Checksum(ProjectorTestCase.fileIdOrg(item.ID))
      logger.debug("Checksum  CLC[%s]".format(checksumClc))
      logger.debug("Checksum  ORG[%s]".format(checksumOrg))
      assert(checksumClc==checksumOrg)
  }
}
