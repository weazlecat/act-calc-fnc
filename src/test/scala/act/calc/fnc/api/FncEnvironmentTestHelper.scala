package act.calc.fnc.api

import act.calc.com.FileIdPath
import act.calc.com.FileIdPath.Location
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object FncEnvironmentTestHelper {

  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  private val testFilenameOfConfigurationFnc: String = "fnc_test.conf"
  private val testFilepathOfTestCases: String ="act/calc/fnc/test-cases/"
  private val testFilelocationOfTestCases: Location = Location.RUN

  val testFileIdPathOfTestCases: FileIdPath = FileIdPath(testFilepathOfTestCases, testFilelocationOfTestCases)
  val testFncConfig: FncConfig = new FncConfig(testFilenameOfConfigurationFnc)

  logger.info("Filename of Finance Configuration - FILENAME[%s]".format(testFilenameOfConfigurationFnc))
}
