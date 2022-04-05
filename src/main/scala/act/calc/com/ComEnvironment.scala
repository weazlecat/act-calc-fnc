package act.calc.com

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import java.util.Objects

object ComEnvironment:

  private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  enum RuntimeEnvironment:
    case IDE
    case JAR
    case NONE
  end RuntimeEnvironment

  private val protocol: String = this.getClass.getResource("").getProtocol

  val runtimeEnvironment: RuntimeEnvironment =
    if (Objects.equals(protocol, "jar"))
      RuntimeEnvironment.JAR
    else if (Objects.equals(protocol, "file"))
      RuntimeEnvironment.IDE
    else
      RuntimeEnvironment.NONE

  logger.info("Runtime Environment - EXECUTION[%s]".format(runtimeEnvironment))

end ComEnvironment
