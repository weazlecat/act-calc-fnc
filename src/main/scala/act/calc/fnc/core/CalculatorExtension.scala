package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate

trait CalculatorExtension{
  def yieldRate(date: ShortDate): Double
  def streetDuration(date: ShortDate): Double
  def swapRate(date: ShortDate, maturity: Int): Double
}
