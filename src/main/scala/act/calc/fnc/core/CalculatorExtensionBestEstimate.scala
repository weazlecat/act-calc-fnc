package act.calc.fnc.core

import act.calc.com.shortdate._
import act.calc.fnc.api.FncConfig
import act.calc.fnc.data.{StockPool, StockRecord}
import act.calc.com.frame.count.CountingShortDate
import org.apache.commons.math3.analysis.UnivariateFunction
import org.apache.commons.math3.optim.MaxEval
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType
import org.apache.commons.math3.optim.univariate.{BrentOptimizer, SearchInterval, UnivariateObjectiveFunction}

import scala.annotation.tailrec

class CalculatorExtensionBestEstimate(stock: StockPool, conf: FncConfig.CalcConf) extends CalculatorExtension {

  private val lastStockRecord: StockRecord = stock.last
  private val firstStockRecord: StockRecord = stock.first
  private val dateOfStockFrom = ShortDate(firstStockRecord.DATE)
  private val dateOfStockTo = ShortDate(lastStockRecord.DATE)

  private val streetDurationMean: Double = stock.meanValue(StockRecord.streetDurationCol)
  private val streetDurationAdjustment: Double = optimize(stock.column(StockRecord.streetDurationCol))
  private val spreadMean: Double = {
    val spreadVector: Array[Double] = new Array[Double](stock.count.toInt)
    for (m <- spreadVector.indices) {
      spreadVector(m) = spreadIboxxYieldToSwapRateAtIboxxStreetDuration(dateOfStockFrom + new ShortDate(0, m))
    }
    spreadVector.sum / spreadVector.length
  }
  private val spreadAdjustment: Double = {
    val spreadVector: Array[Double] = new Array[Double](stock.count.toInt)
    for (m <- spreadVector.indices) {
      spreadVector(m) = spreadIboxxYieldToSwapRateAtIboxxStreetDuration(dateOfStockFrom + new ShortDate(0,m))
    }
    optimize(spreadVector)
  }

  class Objective(val meanValue: Double, val elements: Array[Double]) extends UnivariateFunction {
    def value(adj: Double): Double = vasicekDistance(adj, meanValue, elements)
  }

  private def optimize(elements: Array[Double]): Double = {
    val meanValue = elements.sum / elements.length
    val optimizer: BrentOptimizer = new BrentOptimizer(conf.optimization.relativeTolerance, conf.optimization.absoluteTolerance)
    val objective: UnivariateObjectiveFunction = new UnivariateObjectiveFunction(new Objective(meanValue, elements))
    val maxEval: MaxEval = new MaxEval(conf.optimization.maxEval)
    val searchInterval: SearchInterval = new SearchInterval(conf.optimization.searchIntervalMin, conf.optimization.searchIntervalMax)
    optimizer.optimize(maxEval, objective, GoalType.MINIMIZE, searchInterval).getPoint
  }

  private def vasicekDistance(adjustment: Double, meanValue: Double, elements: Array[Double]): Double = {
    var distance: Double = 0.0
    for (m <- 0 until elements.length-1)
      distance += scala.math.pow(elements(m+1) - elements(m) - adjustment * (meanValue - elements(m)), 2)
    distance
  }

  private def stat(k: Int, start: Double, adjust: Double, mean: Double): Double =
    scala.math.exp(-adjust * k) * (start + mean * (scala.math.exp(adjust * k) -1))

  def streetDuration(date: ShortDate): Double = {
    val m = ShortDate.distance(dateOfStockTo, date)
    if (m<=0)
      stock.select(date).STREET_DURATION
    else {
      val adj = streetDurationAdjustment
      val std = lastStockRecord.STREET_DURATION
      val mea = streetDurationMean
      stat(m, std, adj, mea)
    }
  }

  def yieldRate(date: ShortDate): Double = {
    val m = ShortDate.distance(dateOfStockTo, date)
    if (m<=0)
      stock.select(date).YIELD_RATE
    else {
      val adj = spreadAdjustment
      val std = spreadIboxxYieldToSwapRateAtIboxxStreetDuration(dateOfStockTo)
      val mea = spreadMean
      val spread = stat(m, std, adj, mea)
      val swap = swapRateAtStreetDuration(date)
      spread + swap
    }
  }

  def spreadIboxxYieldToSwapRateAtIboxxStreetDuration(date: ShortDate): Double = {
    yieldRate(date) - swapRateAtStreetDuration(date)
  }

  def swapRateAtStreetDuration(date: ShortDate): Double = {
    val streetDurationValue: Double = streetDuration(date)
    val streetDurationValueMin: Int = streetDurationValue.floor.toInt
    val streetDurationValueMax: Int = streetDurationValue.ceil.toInt
    val swapRateAtStreetDurationMin: Double = swapRate(date, streetDurationValueMin)
    val swapRateAtStreetDurationMax: Double = swapRate(date, streetDurationValueMax)
    swapRateAtStreetDurationMin + (streetDurationValue - streetDurationValueMin) * (swapRateAtStreetDurationMax - swapRateAtStreetDurationMin)
  }

  def swapRate(date: ShortDate, maturity: Int): Double = {
    val m = ShortDate.distance(dateOfStockTo, date)
    if (m<=0)
      stock.select(date).swap(maturity)
    else {
      val j:Int = (m - 1) / 12
      val k:Int = m - 12 * j
      val sj0: Double = forwardRateFromLastStockRecord(j, maturity)
      val sj1: Double = forwardRateFromLastStockRecord(j + 1, maturity)
      ((12 -k) * sj0 + k * sj1) / 12
    }
  }

  @tailrec
  private def forwardRateFromLastStockRecord(distance: Int, maturity: Int): Double = {
    if (distance + maturity > StockRecord.pw) forwardRateFromLastStockRecord(distance-1, maturity)
    else {
      if (maturity==0)
        0.0
      else {
        val s1 = swapRatePresentValueFromLastStockRecord(distance)
        val s2 = swapRatePresentValueFromLastStockRecord(distance+maturity)
        scala.math.pow(s1 / s2, 1.0 / maturity) - 1.0
      }
    }
  }

  private def swapRatePresentValueFromLastStockRecord(maturity: Int): Double = {
    1.0 / scala.math.pow(1.0 + lastStockRecord.swap(maturity), maturity)
  }

}
