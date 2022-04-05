package act.calc.com.frame.immu

import act.calc.com.frame.muta
import act.calc.com.frame.count.CountingInt
import act.calc.com.frame.count.CountingShortDate
import act.calc.com.shortdate.ShortDate
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory

import scala.math.BigDecimal.RoundingMode

class DatVectorFixTest extends AnyFunSuite:

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("DatVectorFix this 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    assert(d1(9) == 2.0)
  }

  test("DatVectorFix Apply/Update 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0)))
    assert(d1(9)==d2(8))
  }

  test("DatVectorFix contains 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0)))
    assert(d1.contains(8))
  }

  test("DatVectorFix contains 02") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0)))
    assert(!d1.contains(9))
  }

  test("DatVectorFix contains 03") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0)))
    assert(!d1.contains(7) & !d1.contains(11))
  }

  test("DatVectorFix FirstIndex / LastIndex 01") {
    val d1 = new DatVectorFix[ShortDate,Double](Iterator((ShortDate(2000, 9), 3.0)))
    val d2 = new DatVectorFix[ShortDate,Double](Iterator((ShortDate(2000, 9), 2.0), (ShortDate(2000, 8), 3.0), (ShortDate(2000, 2), 3.0)))
    assert(d1.firstIndex==d2.lastIndex)
  }

  test("DatVectorFix FirstValue / LastValue 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0), (2, 1.0)))
    val d2 = new DatVectorFix[Int,Double](Iterator((9, 1.0), (8, 3.0), (2, 3.0)))
    assert(d1.firstValue==d2.lastValue)
  }

  test("DatVectorFix indicesAll 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    val i1 = d1.indicesAll
    assert(i1.nonEmpty & i1.head == 7 & i1.last == 9 & i1.contains(8) )
  }

  test("DatVectorFix indicesContains 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    val i1 = d1.indicesContains
    assert(i1.nonEmpty & i1.head == 7 & i1.last == 9 & !i1.contains(8) )
  }

  test("DatVectorFix isEmpty 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0), (2, 1.0)))
    assert(!d1.isEmpty)
  }

  test("DatVectorFix isEmpty 02") {
    val d1 = new DatVectorFix[Int,Double]()
    assert(d1.isEmpty)
  }

  test("DatVectorFix isIndex 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0), (2, 1.0)))
    assert(!d1.isIndex(1) && d1.isIndex(2) && d1.isIndex(3) && d1.isIndex(9), !d1.isIndex(10))
  }

  test("DatVectorFix iterator 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0),  (9, 1.0)))
    val it = d1.iterator
    val i = it.next()
    assert(it.size==2 && i._1==8 && i._2==3)
  }

  test("DatVectorFix range 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.range(9, 11)
    assert(d2.firstIndex==9 && d2.lastIndex==10)
  }

  test("DatVectorFix rangeFrom 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeFrom(9)
    assert(d2.firstIndex==9 && d2.lastIndex==11)
  }

  test("DatVectorFix rangeFrom 02") {
    val d1 = new DatVectorFix[Int,Double]()
    val d2 = d1.rangeFrom(9)
    assert(d2.isEmpty)
  }

  test("DatVectorFix rangeTo 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeTo(10)
    assert(d2.firstIndex==8 && d2.lastIndex==10)
  }

  test("DatVectorFix rangeUntil 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeUntil(10)
    assert(d2.firstIndex==8 && d2.lastIndex==9)
  }

  test("DatVectorFix Size 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorFix[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    assert(d1.size==1 && d2.size==2)
  }

  test("DatVectorFix Size 02") {
    val d1 = new DatVectorFix[Int,Double]()
    assert(d1.size==0)
  }

  test("DatVectorFix round 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.123456), (11, -1.123456),  (12, 5.123456)))
    val d2 = d1.round(3, RoundingMode.HALF_UP)
    assert(d2(10) == 2.123)
  }

  test("DatVectorFix round 02") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.123456), (11, -1.123456),  (12, 5.123456)))
    val d2 = d1.round(3, RoundingMode.HALF_EVEN)
    assert(d2(10) == 2.123)
  }

  test("DatVectorFix round 03") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.123456), (11, -1.123456),  (12, 5.123456)))
    val d2 = d1.round(3, RoundingMode.UNNECESSARY)
    assert(d2(10) == 2.123456)
  }

  test("DatVectorFix + 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorFix[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    val d3 = d1 + d2
    assert(d3(9)==5.0 & d3(8)==3.0)
  }

  test("DatVectorFix ++ 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((11, 3.0)))
    val d2 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (9, 3.0)))
    val d3 = d1 ++ d2
    assert(d3.firstIndex==9 && d3.lastIndex==11)
  }

  test("DatVectorFix toDatColumnBuf 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.123456), (11, -1.123456),  (12, 5.123456)))
    val d2 = d1.toDatVectorBuf
    assert(d2(10) == 2.123456)
  }

  test("DatVectorFix toString 01") {
    val d1 = new DatVectorFix[Int,Double](Iterator((10, 2.0), (8, 3.0),  (9, 1.0)))
    logger.info("DatVectorFix.toString%n".format() + d1.toString)
    assert(true)
  }

end DatVectorFixTest