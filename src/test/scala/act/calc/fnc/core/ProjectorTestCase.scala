package act.calc.fnc.core

import act.calc.com.FileId
import act.calc.com.utils.UtilFile
import act.calc.fnc.api.FncEnvironmentTestHelper
import act.calc.fnc.data.{DataService, StockPool}

object ProjectorTestCase {

  private val testRecordFilename = "proj_test.csv"
  private val testRecordFileId = FileId(testRecordFilename, FncEnvironmentTestHelper.testFileIdPathOfTestCases)
  private val testTemplateFilename: String = UtilFile.removeFileExtension(testRecordFilename, removeAllExtensions = false)

  private def filenameClc(id: String): String = "%s_%s_cal.csv".format(testTemplateFilename, id)
  private def filenameOrg(id: String): String = "%s_%s_org.csv".format(testTemplateFilename, id)

  val testStockData: StockPool = new DataService(FncEnvironmentTestHelper.testFncConfig.stock.csv.fileId, FncEnvironmentTestHelper.testFncConfig.stock.csv.delimiter(0)).data
  val testRecordList: List[ProjectorTestRecord] = UtilFile.csvRead[ProjectorTestRecord](testRecordFileId, ';')

  def fileIdClc(id: String): FileId = FileId(filenameClc(id), FncEnvironmentTestHelper.testFncConfig.proj.csv.fileIdPath)
  def fileIdOrg(id: String): FileId = FileId(filenameOrg(id), FncEnvironmentTestHelper.testFileIdPathOfTestCases)

}
