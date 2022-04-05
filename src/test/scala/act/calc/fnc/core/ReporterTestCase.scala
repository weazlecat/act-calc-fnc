package act.calc.fnc.core

import act.calc.com.FileId
import act.calc.com.utils.UtilFile
import act.calc.fnc.api.FncEnvironmentTestHelper
import act.calc.fnc.data.{DataService, StockPool}

object ReporterTestCase {

  private val testRecordFilename = "repo_test.csv"
  private val testRecordFileId = FileId(testRecordFilename, FncEnvironmentTestHelper.testFileIdPathOfTestCases)
  private val testTemplateFilename: String = UtilFile.removeFileExtension(testRecordFilename, removeAllExtensions = false)

  private def filenameClc(id: String): String = "%s_%s_cal.pdf".format(testTemplateFilename, id)
  private def filenameOrg(id: String): String = "%s_%s_org.pdf".format(testTemplateFilename, id)

  val testStockData: StockPool = new DataService(FncEnvironmentTestHelper.testFncConfig.stock.csv.fileId, FncEnvironmentTestHelper.testFncConfig.stock.csv.delimiter(0)).data
  val testRecordList: List[ReporterTestRecord] = UtilFile.csvRead[ReporterTestRecord](testRecordFileId, ';')

  def fileIdClc(id: String): FileId = FileId(filenameClc(id), FncEnvironmentTestHelper.testFncConfig.repo.pdf)
  def fileIdOrg(id: String): FileId = FileId(filenameOrg(id), FncEnvironmentTestHelper.testFileIdPathOfTestCases)

}
