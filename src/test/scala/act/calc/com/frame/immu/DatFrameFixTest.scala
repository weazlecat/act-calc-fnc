package act.calc.com.frame.immu

import com.typesafe.scalalogging.Logger
import org.scalatest.funsuite.AnyFunSuite
import org.slf4j.LoggerFactory
import act.calc.com.frame.count.CountingInt
import act.calc.com.frame.muta.DatFrameBuf

class DatFrameFixTest extends AnyFunSuite:

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  test("DatFrameFix this 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    assert(d1(2, 12) == 4.0)
  }

  test("DatFrameFix this 02") {
    val d1 = new DatFrameFix[Int,Int,Double]()
    assert(true)
  }

  test("DatFrameFix column 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.column(10)(2) == 3.0)
  }

  test("DatFrameFix columnsAll 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.columnsAll.toList == List(10, 11, 12))
  }

  test("DatFrameFix columnsContains 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.columnsContains.toList == List(10, 12))
  }

  test("DatFrameFix firstColumn 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.firstColumn == 10)
  }

  test("DatFrameFix firstRow 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    assert(d1.firstRow == 1)
  }

  test("DatFrameFix isColumn 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.isColumn(12))
  }

  test("DatFrameFix isEmpty 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    val d2 = new DatFrameFix[Int,Int,Double]()
    assert(!d1.isEmpty && d2.isEmpty)
  }

  test("DatFrameFix isRow 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    assert(d1.isRow(2) && !d1.isRow(3))
  }

  test("DatFrameFix iterator 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    val it = d1.iterator
    val i = it.next()
    assert(it.size==2 && i._1 == 1 && i._2 == 10 && i._3 == 1.0)
  }

  test("DatFrameFix lastColumn") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0)))
    assert(d1.lastColumn == 12)
  }

  test("DatFrameFix lastRow 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    assert(d1.lastRow == 5)
  }

  test("DatFrameFix row 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    val r1 = d1.row(2)
    assert(r1(10) == 3.0 && r1.isIndex(11))
  }

  test("DatFrameFix rowsAll 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    val r1 = d1.rowsAll
    assert(r1.head == 1 && r1.last==5 && r1.contains(3))
  }

  test("DatFrameFix rowsContains 01") {
    val d1 = new DatFrameFix[Int,Int,Double](Iterator((1, 10, 1.0), (2, 10, 3.0), (2, 12, 4.0), (5, 2, 5.0)))
    val r1 = d1.rowsContains
    assert(r1.head == 1 && r1.last==5 && !r1.contains(3))
  }

end DatFrameFixTest