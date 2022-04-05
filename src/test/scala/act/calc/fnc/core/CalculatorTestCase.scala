package act.calc.fnc.core

import act.calc.com.FileId
import act.calc.com.shortdate.ShortDate
import act.calc.com.utils.UtilFile
import act.calc.fnc.api.FncEnvironmentTestHelper
import act.calc.fnc.data.{DataService, StockPool}

object CalculatorTestCase {

  private val testRecordFilename = "calc_test.csv"
  private val testRecordFileId = FileId(testRecordFilename, FncEnvironmentTestHelper.testFileIdPathOfTestCases)

  val testStockData: StockPool = new DataService(FncEnvironmentTestHelper.testFncConfig.stock.csv.fileId, FncEnvironmentTestHelper.testFncConfig.stock.csv.delimiter(0)).data
  val testRecordList: List[CalculatorTestRecord] = UtilFile.csvRead[CalculatorTestRecord](testRecordFileId, ';')

  def calcRecord(item: CalculatorTestRecord): CalcRecord = {
    CalcRecord(
      ShortDate(item.DATE).toString,
      item.YIELD_RATE,
      item.STREET_DURATION,
      item.SWAP_RATE_AT_STREET_DURATION,
      item.SPREAD_AT_STREET_DURATION,
      item.YIELD_AVERAGE,
      item.STREET_DURATION_AVERAGE,
      item.SWAP_RATE_AT_STREET_DURATION_AVERAGE,
      item.SPREAD_AT_STREET_DURATION_AVERAGE,
      item.SWAP_RATE,
      item.SWAP_RATE_AVERAGE,
      item.HGB_RATE,
      item.HGB_RATE_DELTA
    )
  }

}
