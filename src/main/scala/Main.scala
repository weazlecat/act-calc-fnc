
import act.calc.com.shortdate.*
import act.calc.fnc.api.FncService
import act.calc.fnc.core.Calculator
import scopt.OParser

import scala.math.Numeric.Implicits.infixNumericOps

object Main {

  //private val logger = Logger(LoggerFactory.getLogger(this.getClass))

  case class ConfigArgs(
    dsf: String = "",
    dst: String = "",
    drf: String = "",
    fos: String = "",
    too: String = ""
  )

  case class ConfigParms(
    dateOfStockFrom: ShortDate,
    dateOfStockTo: ShortDate,
    dateOfReportFrom: ShortDate,
    dateOfReportTo: ShortDate,
    dateOfProjectionTo: ShortDate,
    filenameOfStock: String,
    filetemplateOfOutput: String,
    maturity: Int,
    averageYearsA: Int,
    averageYearsB: Int
  )

  def setParms(args: ConfigArgs): ConfigParms = {
    val parms = ConfigParms(
      dateOfStockFrom = ShortDate(args.dsf),
      dateOfStockTo = ShortDate(args.dst),
      dateOfReportFrom = ShortDate(args.drf),
      dateOfReportTo = ShortDate(args.drf) + 120,
      dateOfProjectionTo = ShortDate(args.drf) + 144,
      filenameOfStock = args.fos,
      filetemplateOfOutput = args.too,
      maturity = 15,
      averageYearsA = 10,
      averageYearsB = 7
    )
    parms
  }

  def showParms(parm: ConfigParms): Unit = {
    println("-- Parameter settings:")
    println("Date of stock from:        [%s]".format(parm.dateOfStockFrom.toString))
    println("Date of stock to:          [%s]".format(parm.dateOfStockTo.toString))
    println("Date of report from:       [%s]".format(parm.dateOfReportFrom.toString))
    println("Date of report to:         [%s]".format(parm.dateOfReportTo.toString))
    println("Date of projection to:     [%s]".format(parm.dateOfProjectionTo.toString))
    println("Filename of stock:         [%s]".format(parm.filenameOfStock))
    println("Filetemplate of output:    [%s]".format(parm.filetemplateOfOutput))
    println("Maturity of report:        [%d]".format(parm.maturity))
    println("Average in Years Main (A): [%d]".format(parm.averageYearsA))
    println("Average in Years 2nd  (B): [%d]".format(parm.averageYearsB))
    println()
  }

  def filenameOfProjection(dateOfReport: ShortDate, average: Int, projection: Calculator.ProjectionAttrib, template: String): String = {
    "%s_%s_%s_%03d.csv".format(dateOfReport.toString, template, projection.toString.toLowerCase.substring(0, 4), average)
  }

  def filenameOfReport(dateOfReport: ShortDate, projection: Calculator.ProjectionAttrib, template: String): String = {
    "%s_%s_%s.pdf".format(dateOfReport.toString, template, projection.toString.toLowerCase.substring(0, 4))
  }

  def makeProjection(service: FncService, parms: ConfigParms, projection: Calculator.ProjectionAttrib, averageId: String): Unit = {
    val average = averageId match {
      case "A" => parms.averageYearsA * 12
      case "B" => parms.averageYearsB * 12
    }
    val filename = filenameOfProjection(parms.dateOfReportFrom, average, projection, parms.filetemplateOfOutput)
    service.projectToFile(
      projection = projection,
      dateOfStockFrom = parms.dateOfStockFrom,
      dateOfStockTo = parms.dateOfStockTo,
      dateOfReportFrom = parms.dateOfReportFrom,
      dateOfReportTo = parms.dateOfProjectionTo,
      average = average,
      filenameOfProjection = filename
    )
    println("Filename of projection: [%s]".format(filename))
  }

  def makeReport(service: FncService, parms: ConfigParms, projection: Calculator.ProjectionAttrib): Unit = {
    val filename = filenameOfReport(parms.dateOfReportFrom, projection, parms.filetemplateOfOutput)
    service.reportToFile(
      projection = projection,
      dateOfStockFrom = parms.dateOfStockFrom,
      dateOfStockTo = parms.dateOfStockTo,
      dateOfReportFrom = parms.dateOfReportFrom,
      dateOfReportTo = parms.dateOfReportTo,
      maturity = parms.maturity,
      averageYearsA = parms.averageYearsA,
      averageYearsB = parms.averageYearsB,
      filenameOfReport = filename
    )
    println("Filename of report: [%s]".format(filename))
  }

  private val builder = OParser.builder[ConfigArgs]
  private val parser = {
    OParser.sequence(
      builder.programName("act-calc-fnc"),
      //builder.head("act-calc-fnc", "1.0"),
      builder.note("Forecast of the yield curve for the discounting of provisions (HGB)\n"),
      builder.opt[String]("dsf")
        .action((x, c) => c.copy(dsf = x))
        .text("Date of stock from [yyyy-MM] f.e. [2002-01]")
        .required(),
      builder.opt[String]("dst")
        .action((x, c) => c.copy(dst = x))
        .text("Date of stock to [yyyy-MM] f.e [2020-12]")
        .required(),
      builder.opt[String]("drf")
        .action((x, c) => c.copy(drf = x))
        .text("Date of report from [yyyy-MM] f.e [2020-12]")
        .required(),
      builder.opt[String]("fos")
        .action((x, c) => c.copy(fos = x))
        .text("Filename of stock f.e. [stock.csv]")
        .required(),
      builder.opt[String]("too")
        .action((x, c) => c.copy(too = x))
        .text("Template of output file f.e. [report]")
        .required(),
    )
  }

  def main(args: Array[String]): Unit = OParser.parse(parser, args, ConfigArgs()) match {
    case Some(config) =>
      val parms = setParms(config)
      val service = new FncService()
      showParms(parms)
      makeProjection(service, parms, Calculator.ProjectionAttrib.Flat, "A")
      makeProjection(service, parms, Calculator.ProjectionAttrib.Flat, "B")
      makeProjection(service, parms, Calculator.ProjectionAttrib.BestEstimate, "A")
      makeProjection(service, parms, Calculator.ProjectionAttrib.BestEstimate, "B")
      makeReport(service, parms, Calculator.ProjectionAttrib.Flat)
      makeReport(service, parms, Calculator.ProjectionAttrib.BestEstimate)
    case _ =>
    // arguments are bad, error message will have been displayed
  }

}
