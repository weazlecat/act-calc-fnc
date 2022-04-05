package act.calc.com.utils

import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import java.io.{File, InputStream}
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.nio.file.Path

object UtilHelper:

  private val eps = 0.00000001

  def equalDouble(v1: String, v2: String): Boolean =
    val o1: Option[Double] = v1 match
      case "" => None
      case _ => Option[Double](v1.toDouble)
    val o2: Option[Double] = v2 match
      case "" => None
      case _ => Option[Double](v2.toDouble)
    if o1.isDefined && o2.isDefined then
      val o = o1.get - o2.get
      o + eps > 0 && o - eps < 0
    else
      o1 == o2

  def equalDouble(v1: Double, v2: Double): Boolean =
    val o = v1 - v2
    o + eps > 0 && o - eps < 0

  def equalInt(v1: String, v2: String): Boolean =
    val o1: Option[Int] = v1 match
      case "" => None
      case _ => Option[Int](v1.toInt)
    val o2: Option[Int] = v2 match
      case "" => None
      case _ => Option[Int](v2.toInt)
    o1 == o2

end UtilHelper
