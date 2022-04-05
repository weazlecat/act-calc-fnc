package act.calc.fnc.api

import act.calc.com.shortdate.ShortDate
import act.calc.fnc.core.{Calculator, Projector, Reporter}
import act.calc.fnc.data.{DataService, StockPool}

class FncService(val conf: FncConfig = new FncConfig(FncEnvironment.filenameOfConfigurationFnc)):

  private val stockDataStd: StockPool = new DataService(conf.stock.csv.fileId, conf.stock.csv.delimiter(0)).data

  def calculator(stockData: StockPool = stockDataStd, projection: Calculator.ProjectionAttrib, dateOfStockFrom: ShortDate, dateOfStockTo: ShortDate) : Calculator =
    new Calculator(stockData, projection, dateOfStockFrom, dateOfStockTo, conf.calc)

  def projector(stockData: StockPool = stockDataStd, projection: Calculator.ProjectionAttrib, dateOfStockFrom: ShortDate, dateOfStockTo: ShortDate): Projector =
    val calc = calculator(stockData, projection, dateOfStockFrom, dateOfStockTo)
    new Projector(calc, conf.proj)

  def reporter(stockData: StockPool = stockDataStd, projection: Calculator.ProjectionAttrib, dateOfStockFrom: ShortDate, dateOfStockTo: ShortDate): Reporter =
    val calc = calculator(stockData, projection, dateOfStockFrom, dateOfStockTo)
    new Reporter(calc, conf.repo)

  def projectToFile(stockData: StockPool = stockDataStd, projection: Calculator.ProjectionAttrib, dateOfStockFrom: ShortDate, dateOfStockTo: ShortDate, dateOfReportFrom: ShortDate, dateOfReportTo: ShortDate, average: Int, filenameOfProjection: String): Unit =
    val projector: Projector = this.projector(stockData, projection, dateOfStockFrom, dateOfStockTo)
    projector.project(dateOfReportFrom, dateOfReportTo, average, filenameOfProjection)

  def reportToFile(stockData: StockPool = stockDataStd, projection: Calculator.ProjectionAttrib, dateOfStockFrom: ShortDate, dateOfStockTo: ShortDate, dateOfReportFrom: ShortDate, dateOfReportTo: ShortDate, maturity: Int, averageYearsA: Int, averageYearsB: Int, filenameOfReport: String): Unit =
    val reporter: Reporter = this.reporter(stockData, projection, dateOfStockFrom, dateOfStockTo)
    reporter.report(dateOfReportFrom, dateOfReportTo, maturity, averageYearsA, averageYearsB, filenameOfReport)

end FncService
