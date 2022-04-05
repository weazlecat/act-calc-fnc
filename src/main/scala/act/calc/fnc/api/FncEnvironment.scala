package act.calc.fnc.api

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

object FncEnvironment:

  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  // ----- Standard Configuration
  val filenameOfConfigurationFnc = "fnc.conf"

  logger.info("Filename of Financial Configuration - FILENAME[%s]".format(filenameOfConfigurationFnc))

end FncEnvironment
