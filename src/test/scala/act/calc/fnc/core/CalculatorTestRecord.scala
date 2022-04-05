package act.calc.fnc.core

case class CalculatorTestRecord(
  ID: String,
  MATURITY: Int,
  AVERAGE: Int,
  PROJECTION: String,
  DATE_OF_STOCK_FROM: String,
  DATE_OF_STOCK_TO: String,
  DATE_OF_PROJECTION_TO: String,
  DATE: String,
  YIELD_RATE: Double,
  STREET_DURATION: Double,
  SWAP_RATE_AT_STREET_DURATION: Double,
  SPREAD_AT_STREET_DURATION: Double,
  YIELD_AVERAGE: Double,
  STREET_DURATION_AVERAGE: Double,
  SWAP_RATE_AT_STREET_DURATION_AVERAGE: Double,
  SPREAD_AT_STREET_DURATION_AVERAGE: Double,
  SWAP_RATE: Double,
  SWAP_RATE_AVERAGE: Double,
  HGB_RATE: Double,
  HGB_RATE_DELTA: Double
)
