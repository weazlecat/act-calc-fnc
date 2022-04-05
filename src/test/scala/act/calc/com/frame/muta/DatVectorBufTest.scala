package act.calc.com.frame.muta

import act.calc.com.frame.immu

import math.Numeric.Implicits.infixNumericOps
import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory
import act.calc.com.frame.count.CountingInt
import act.calc.com.frame.count.CountingShortDate
import act.calc.com.shortdate.ShortDate


import scala.math.BigDecimal.RoundingMode

class DatVectorBufTest extends AnyFunSuite:

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("DatVectorBuf this 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    assert(d1(9) == 2.0)
  }

  test("DatVectorBuf this 02") {
    val d1 = new DatVectorBuf[ShortDate,Double](Iterator((new ShortDate(2000,10), 2.0), (new ShortDate(2001,1), 3.0)))
    assert(d1(new ShortDate(2000,10)) == 2.0)
  }

  test("DatVectorBuf Size 01") {
    val d1 = new DatVectorBuf[Int,Double]
    val d2 = new DatVectorBuf[Int,Double]
    d2(10) = 100.0
    assert(d1.size == 0 && d2.size == 1)
  }

  test("DatVectorBuf Size 02") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    assert(d1.size==1 && d2.size==2)
  }

  test("DatVectorBuf Apply/Update 01") {
    val d1 = new DatVectorBuf[Int,Double]
    val d2 = new DatVectorBuf[Int,Double]
    d1(10) = 100.0
    d2(11) = 100.0
    assert(d1(10)==d2(11))
  }

  test("DatVectorBuf Apply/Update 02") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    assert(d1(9)==d2(8))
  }

  test("DatVectorBuf contains 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    assert(d1.contains(7))
  }

  test("DatVectorBuf contains 02") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    assert(!d1.contains(8))
  }

  test("DatVectorBuf contains 03") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    assert(!d1.contains(10) & !d1.contains(6))
  }

  test("DatVectorBuf FirstIndex / LastIndex 01") {
    val d1 = new DatVectorBuf[Int,Double]
    val d2 = new DatVectorBuf[Int,Double]
    d1(10) = 1
    d1(100)= 2
    d2(1) = 3
    d2(10) = 4
    assert(d1.firstIndex==d2.lastIndex)
  }

  test("DatVectorBuf FirstValue / LastValue 01") {
    val d1 = new DatVectorBuf[Int,Double]
    val d2 = new DatVectorBuf[Int,Double]
    d1(10) = 1
    d1(100) = 0
    d2(1) = 0
    d2(10) = 1
    assert(d1.firstValue==d2.lastValue)
  }

  test("DatVectorBuf indicesAll 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    val i1 = d1.indicesAll
    assert(i1.nonEmpty & i1.head == 7 & i1.last == 9 & i1.contains(8) )
  }

  test("DatVectorBuf indicesContains 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    val i1 = d1.indicesContains
    assert(i1.nonEmpty & i1.head == 7 & i1.last == 9 & !i1.contains(8) )
  }

  test("DatVectorBuf isEmpty 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (7, 3.0)))
    assert(!d1.isEmpty)
  }

  test("DatVectorBuf isEmpty 02") {
    val d1 = new DatVectorBuf[Int,Double]()
    assert(d1.isEmpty)
  }

  test("DatVectorBuf isIndex 01") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 1
    d1(100) = 0
    assert(!d1.isIndex(9) && d1.isIndex(10) && d1.isIndex(100) && !d1.isIndex(101))
  }

  test("DatVectorBuf iterator 01") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 2
    d1(11) = -1
    d1(12) = 5
    val it = d1.iterator
    val i = it.next()
    assert(it.size==2 && i._1==10 && i._2==2)
  }

  test("DatVectorBuf range 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.range(9, 11)
    assert(d2.firstIndex==9 && d2.lastIndex==10)
  }

  test("DatVectorBuf rangeFrom 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeFrom(9)
    assert(d2.firstIndex==9 && d2.lastIndex==11)
  }

  test("DatVectorBuf rangeFrom 02") {
    val d1 = new DatVectorBuf[Int,Double]()
    val d2 = d1.rangeFrom(9)
    assert(d2.isEmpty)
  }

  test("DatVectorBuf rangeTo 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeTo(10)
    assert(d2.firstIndex==8 && d2.lastIndex==10)
  }

  test("DatVectorBuf rangeUntil 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((10, 2.0), (11, 4.0), (8, 3.0),  (9, 1.0)))
    val d2 = d1.rangeUntil(10)
    assert(d2.firstIndex==8 && d2.lastIndex==9)
  }

  test("DatVectorBuf round 01") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 2.123456
    d1(11) = -1.123456
    d1(12) = 5.123456
    val d2 = d1.round(3, RoundingMode.HALF_UP)
    assert(d2(10) == 2.123)
  }

  test("DatVectorBuf round 02") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 2.123456
    d1(11) = -1.123456
    d1(12) = 5.123456
    val d2 = d1.round(3, RoundingMode.HALF_EVEN)
    assert(d2(10) == 2.123)
  }

  test("DatVectorBuf round 03") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 2.123456
    d1(11) = -1.123456
    d1(12) = 5.123456
    val d2 = d1.round(3, RoundingMode.UNNECESSARY)
    assert(d2(10) == 2.123456)
  }

  test("DatVectorBuf + 01") {
    val d1 = new DatVectorBuf[Int,Double](Iterator((9, 3.0)))
    val d2 = new DatVectorBuf[Int,Double](Iterator((9, 2.0), (8, 3.0)))
    val d3 = d1 + d2
    assert(d3(9)==5.0 & d3(8)==3.0)
  }

  test("DatVectorBuf ++ 01") {
    val d1 = new DatVectorBuf[Int,Double]
    val d2 = new DatVectorBuf[Int,Double]
    d1(10) = 1
    d1(100)= 2
    d2(1) = 3
    d2(10) = 4
    val d3 = d1 ++ d2
    assert(d3.firstIndex==1 && d3.lastIndex==100 && d3(10)==4)
  }

  test("DatVectorBuf toDatColumnFix 01") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 2.123456
    d1(11) = -1.123456
    d1(12) = 5.123456
    val d2 = d1.toDatVectorFix
    assert(d2(10) == 2.123456)
  }

  test("DatVectorBuf toString") {
    val d1 = new DatVectorBuf[Int,Double]
    d1(10) = 500
    d1(20) = 600
    logger.info("DatVectorBuf.toString%n".format()+ d1.toString)
    val d2 = new DatVectorBuf[Int,Double]
    logger.info("DatVectorBuf.toString%n".format()+ d2.toString)
    assert(true)
  }

end DatVectorBufTest
