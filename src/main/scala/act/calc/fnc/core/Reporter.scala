package act.calc.fnc.core

import act.calc.com.shortdate.ShortDate
import act.calc.com.FileId
import act.calc.com.utils.UtilFile
import act.calc.fnc.api.FncConfig
import com.typesafe.scalalogging.Logger
import org.jfree.data.time.{Day, TimeSeries, TimeSeriesCollection}
import org.jfree.chart.{ChartFactory, ChartUtils, JFreeChart}
import org.slf4j.LoggerFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import scalatags.Text.all.*
import scala.collection.mutable

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.awt.{Color, Font}

class Reporter (calc: Calculator, conf: FncConfig.ReportConf):

  case class ChartSeries(
    label: String,
    values: List[(ShortDate,Double)]
  )

  case class TableItem01(
    date: String,
    swap_rate_a1: String,
    spread_at_street_duration_a1: String,
    hgb_rate_a1: String,
    hgb_rate_a1_delta: String,
    hgb_rate_a2: String
  )

  case class TableItem02(
    date: String,
    hgb_rate_01: String,
    hgb_rate_02: String,
    hgb_rate_03: String,
    hgb_rate_04: String,
    hgb_rate_05: String,
    hgb_rate_06: String,
    hgb_rate_07: String,
    hgb_rate_08: String,
    hgb_rate_09: String,
    hgb_rate_10: String,
    hgb_rate_11: String,
    hgb_rate_12: String
  )

  private val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass))

  conf.pdf.createDirectory
  conf.template.target.createDirectory

  private val pdfImgSwap: FileId = FileId(conf.template.pdfImgSwap, conf.template.target)
  private val pdfImgSpread: FileId = FileId(conf.template.pdfImgSpread, conf.template.target)
  private val pdfPic: FileId = FileId(conf.template.pdfPic, conf.template.target)
  private val pdfCss: FileId = FileId(conf.template.pdfCss, conf.template.target)

  FileId(conf.template.pdfPic, conf.template.source).copyTo(pdfPic)
  FileId(conf.template.pdfCss, conf.template.source).copyTo(pdfCss)

  private val year0: Map[Int,Seq[Int]] = Map (
    1 -> Seq(-1, 0, 2, 5, 8),
    2 -> Seq(-2, 0, 1, 4, 7),
    3 -> Seq(-3, 0, 3, 6),
    4 -> Seq(-4, 0, 2, 5),
    5 -> Seq(-5, 0, 1, 4),
    6 -> Seq(-6, 0, 3),
    7 -> Seq(-7, 0, 2),
    8 -> Seq(-8, 0, 1),
    9 -> Seq(-9, 0),
    10 -> Seq(-10, -1),
    11 -> Seq(-11, -2),
    12 -> Seq(-12, -3)
  )

  private val distanceOfTable = 120

  def report(dateOfReport: ShortDate, dateOfProjectionTo: ShortDate, maturity: Int, averageYearsA: Int, averageYearsB: Int, filename: String): Unit = {

    val calcResultA = calc.projection(dateOfProjectionTo, maturity, 12 * averageYearsA)
    val calcResultB = calc.projection(dateOfProjectionTo, maturity, 12 * averageYearsB)

    def imgSwap(): Unit =
      logger.info("Running IMG SWAP - BEGIN FILE[%s]".format(pdfImgSwap.toString))
      val series1Label: String = "Swap"
      val series1Value: mutable.ListBuffer[(ShortDate,Double)] = new mutable.ListBuffer
      for date <- ShortDate.range(calc.dateOfStockFrom, dateOfProjectionTo) do
        series1Value += ((date, 100 * calcResultA.select(date).swap_rate))
      val series2Label: String = "ø %d Jahre".format(averageYearsA)
      val series2Value: mutable.ListBuffer[(ShortDate,Double)] = new mutable.ListBuffer
      for date <- ShortDate.range(calc.dateOfStockFrom + 12 * averageYearsA, dateOfProjectionTo) do
        series2Value += ((date, 100 * calcResultA.select(date).swap_rate_average))
      val series1 = ChartSeries(series1Label, series1Value.result)
      val series2 = ChartSeries(series2Label, series2Value.result)
      imgBuild("Null-Kupon-Euro-Swap", "Datum", "Prozent", series1, series2, pdfImgSwap)
      logger.info("Running IMG SWAP - END")

    def imgSpread(): Unit =
      logger.info("Running IMG SPREAD - BEGIN FILE[%s]".format(pdfImgSpread.toString))
      val series1Label: String = "Spread"
      val series1Value: mutable.ListBuffer[(ShortDate,Double)] = new mutable.ListBuffer
      for date <- ShortDate.range(calc.dateOfStockFrom, dateOfProjectionTo) do
        series1Value += ((date, 100 * calcResultA.select(date).spread_at_street_duration))
      val series2Label: String = "ø %d Jahre".format(averageYearsA)
      val series2Value: mutable.ListBuffer[(ShortDate,Double)] = new mutable.ListBuffer
      for date <- ShortDate.range(calc.dateOfStockFrom + 12 * averageYearsA, dateOfProjectionTo) do
        series2Value += ((date, 100 * calcResultA.select(date).spread_at_street_duration_average))
      val series1 = ChartSeries(series1Label, series1Value.result)
      val series2 = ChartSeries(series2Label, series2Value.result)
      imgBuild("Aufschlag Unternehmensanleiheindex (Spread)", "Datum", "Prozent", series1, series2, pdfImgSpread)
      logger.info("Running IMG SPREAD - END")

    def imgBuild(title: String, xAxisLabel: String, yAxisLabel: String, series1: ChartSeries, series2: ChartSeries, imgFileId: FileId): Unit =
      val dataset: TimeSeriesCollection = new TimeSeriesCollection()
      val s1: TimeSeries = new TimeSeries(series1.label)
      val s2: TimeSeries = new TimeSeries(series2.label)
      for v <- series1.values do
        val dat = v._1.toLocalDate
        val day = new Day(dat.getDayOfMonth, dat.getMonthValue, dat.getYear)
        s1.add(day, v._2)
      for v <- series2.values do
        val dat = v._1.toLocalDate
        val day = new Day(dat.getDayOfMonth, dat.getMonthValue, dat.getYear)
        s2.add(day, v._2)
      dataset.addSeries(s1)
      dataset.addSeries(s2)
      val chart: JFreeChart = ChartFactory.createTimeSeriesChart(title, xAxisLabel, yAxisLabel, dataset)
      chart.getTitle.setFont(new Font("Tahoma", Font.BOLD, 12))
      chart.getXYPlot.getRangeAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12))
      chart.getXYPlot.getDomainAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12))
      chart.getXYPlot.setBackgroundPaint(Color.WHITE)
      chart.getXYPlot.setRangeGridlinePaint(Color.BLACK)
      chart.getXYPlot.setDomainGridlinePaint(Color.BLACK)
      chart.getXYPlot.getRenderer.setSeriesPaint(0, new Color(0, 103, 41)) // #006729
      chart.getXYPlot.getRenderer.setSeriesPaint(1, new Color(140, 20, 41)) // #8c1429
      val width: Int = 493  // Width of the image
      val height: Int = 284 // Height of the image
      ChartUtils.saveChartAsPNG(imgFileId.file, chart, width, height)

    def runScalatags(): String = {

      logger.info("Running SCALATAGS - BEGIN: METHOD[%s]".format(calc.projection))
      val distanceOfProjection = ShortDate.distance(calc.dateOfStockTo, dateOfProjectionTo)

      val indexMonth: mutable.ListBuffer[Int] = mutable.ListBuffer()
      val itemsTab01: mutable.ListBuffer[TableItem01] = mutable.ListBuffer()
      val itemsTab02A: mutable.ListBuffer[TableItem02] = mutable.ListBuffer()
      val itemsTab02B: mutable.ListBuffer[TableItem02] = mutable.ListBuffer()

      for (addMonth <- year0(dateOfReport.month))
        if (addMonth <= distanceOfProjection) indexMonth += addMonth
      for (addYear <- 1 to distanceOfTable / 12 + 1)
        val addMonth = 12 * addYear - dateOfReport.month
        if (addMonth <= distanceOfProjection) indexMonth += addMonth
      for (index <- indexMonth)
        val date = dateOfReport + index
        itemsTab01 += TableItem01(
          date.toString,
          "%.2f %%".format(100*calcResultA.select(date).swap_rate_average),
          "%.2f %%".format(100*calcResultA.select(date).spread_at_street_duration_average),
          "%.2f %%".format(100*calcResultA.select(date).hgb_rate),
          "%.2f %%".format(100*calcResultA.select(date).hgb_rate_delta),
          "%.2f %%".format(100*calcResultB.select(date).hgb_rate)
        )
      for (index <- 0 to distanceOfTable / 12)
        if (12 * index + 12 - dateOfReport.month <= distanceOfProjection)
          itemsTab02A += TableItem02(
            "%d".format(dateOfReport.year + index),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 1)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 2)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 3)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 4)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 5)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 6)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 7)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 8)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 9)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 10)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 11)).hgb_rate),
            "%.2f %%".format(100*calcResultA.select(new ShortDate(dateOfReport.year + index, 12)).hgb_rate)
          )
      for (index <- 0 to distanceOfTable / 12)
        if (12 * index + 12 - dateOfReport.month <= distanceOfProjection)
          itemsTab02B += TableItem02(
            "%d".format(dateOfReport.year + index),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 1)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 2)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 3)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 4)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 5)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 6)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 7)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 8)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 9)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 10)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 11)).hgb_rate),
            "%.2f %%".format(100 * calcResultB.select(new ShortDate(dateOfReport.year + index, 12)).hgb_rate)
          )

      val methodString = calc.projection match
        case Calculator.ProjectionAttrib.Flat => "Methode FLAT"
        case Calculator.ProjectionAttrib.BestEstimate => "Methode BEST ESTIMATE"

      val descriptionString = calc.projection match
        case Calculator.ProjectionAttrib.Flat => "Für beide Komponenten Zinsswap und Aufschlag wird der letzte bekannte Stand des Monatsultimos unverändert fortgeschrieben. Dieser Ansatz gibt die Erwartung wieder, dass Zinssatz (Swap) und Aufschlag (Spread) in der Zukunft unverändert bleiben."
        case Calculator.ProjectionAttrib.BestEstimate => "Die Daten der Bundesbank ermöglichen rechnerisch die Ermittlung von Zinsen für die Zukunft (implizite Terminzinssätze). Sie werden in der Prognose als beste Schätzwerte für die Meinung des Marktes für den zukünftigen Zinsswap angesetzt. Für die Prognose des Aufschlags (Spread) wird ein statistisches Verfahren verwendet, das von einem Schwanken der Aufschläge um einen Mittelwert ausgeht (Mean Reversion, Mittelwertrückkehr)."

      val htmlString =
        "<!DOCTYPE html>" +
          html(
            head(
              meta(httpEquiv := "Content-Type", content := "text/html; charset=utf-8"),
              link(rel := "stylesheet", href := conf.template.pdfCss)
            ),
            body(
              h1("Prognose der Abzinsungssätze § 253 Abs. 2 HGB (", maturity, " Jahre Laufzeit)"),
              h1(methodString),
              br,
              br,
              div(cls := "row",
                div(cls:="column",
                  img(src := pdfImgSwap.name),
                  br,
                  br,
                  img(src := pdfImgSpread.name),
                ),
                div(cls:="column",
                  table(cls := "tab_01",
                    thead(
                      tr(
                        th(cls := "t1h11","Datum"),
                        th(cls := "t1h12","Swap", br, "ø", br, averageYearsA, " Jahre"),
                        th(cls := "t1h13","Spread", br, "ø", br, averageYearsA, " Jahre"),
                        th(cls := "t1h14","§ 253 HGB", br, "ø", br, averageYearsA, " Jahre"),
                        th(cls := "t1h15","Δ Vj."),
                        th(cls := "t1h16","§ 253 HGB", br, "ø", br, averageYearsB, " Jahre")
                      ),
                      tr(
                        th(cls := "t1h21","(1)"),
                        th(cls := "t1h22","(2)"),
                        th(cls := "t1h23","(3)"),
                        th(cls := "t1h24","(4)", br, "=(2)+(3)"),
                        th(cls := "t1h25","(5)", br, "=(4)-(4,Vj)"),
                        th(cls := "t1h26","(6)")
                      )
                    ),
                    tbody(
                      for (item <- itemsTab01.toList) yield
                        tr(
                          th(cls := "t1b1", item.date),
                          th(cls := "t1b2", item.swap_rate_a1),
                          th(cls := "t1b3", item.spread_at_street_duration_a1),
                          th(cls := "t1b4", item.hgb_rate_a1),
                          th(cls := "t1b5", item.hgb_rate_a1_delta),
                          th(cls := "t1b6", item.hgb_rate_a2)
                        )
                    )
                  ),
                  p("Der HGB-Zins setzt sich aus den Komponenten Null-Kupon-Euro-Zinsswap (Swap) und Aufschlag (Spread) zusammen. Die Daten basieren auf den Angaben der Bundesbank (Swap) bzw. der von der Bundesbank angegebenen Quellen (Spread)."),
                  p(b(descriptionString)),
                  p(b("Stand ", dateOfReport.toString("dd.MM.yyyy")))
                )
              ),
              div(cls := "pagebreak"),
              h1("Prognose der Abzinsungssätze § 253 Abs. 2 HGB (", maturity, " Jahre Laufzeit)"),
              h1(methodString),
              br,
              h2("Tabelle der Monatswerte: ø ",averageYearsA, " Jahre"),
              table(cls := "tab_02",
                thead(
                  tr(
                    th(cls := "t2h1","Datum", br, "ø ", averageYearsA, " Jahre", br, "Jahr/Monat"),
                    th(cls := "t2h2","01"),
                    th(cls := "t2h3","02"),
                    th(cls := "t2h4","03"),
                    th(cls := "t2h5","04"),
                    th(cls := "t2h6","05"),
                    th(cls := "t2h7","06"),
                    th(cls := "t2h8","07"),
                    th(cls := "t2h9","08"),
                    th(cls := "t2h10","09"),
                    th(cls := "t2h11","10"),
                    th(cls := "t2h12","11"),
                    th(cls := "t2h13","12")
                  ),
                ),
                tbody(
                  for (item <- itemsTab02A.toList) yield
                    tr(
                      th(cls := "t2b1", item.date),
                      th(cls := "t2b2", item.hgb_rate_01),
                      th(cls := "t2b3", item.hgb_rate_02),
                      th(cls := "t2b4", item.hgb_rate_03),
                      th(cls := "t2b5", item.hgb_rate_04),
                      th(cls := "t2b6", item.hgb_rate_05),
                      th(cls := "t2b7", item.hgb_rate_06),
                      th(cls := "t2b8", item.hgb_rate_07),
                      th(cls := "t2b9", item.hgb_rate_08),
                      th(cls := "t2b10", item.hgb_rate_09),
                      th(cls := "t2b11", item.hgb_rate_10),
                      th(cls := "t2b12", item.hgb_rate_11),
                      th(cls := "t2b13", item.hgb_rate_12)
                    )
                )
              ),
              br,
              h2("Tabelle der Monatswerte: ø ",averageYearsB, " Jahre"),
              table(cls := "tab_02",
                thead(
                  tr(
                    th(cls := "t2h1","Datum", br, "ø ", averageYearsB, " Jahre", br, "Jahr/Monat"),
                    th(cls := "t2h2","01"),
                    th(cls := "t2h3","02"),
                    th(cls := "t2h4","03"),
                    th(cls := "t2h5","04"),
                    th(cls := "t2h6","05"),
                    th(cls := "t2h7","06"),
                    th(cls := "t2h8","07"),
                    th(cls := "t2h9","08"),
                    th(cls := "t2h10","09"),
                    th(cls := "t2h11","10"),
                    th(cls := "t2h12","11"),
                    th(cls := "t2h13","12")
                  ),
                ),
                tbody(
                  for (item <- itemsTab02B.toList) yield
                    tr(
                      th(cls := "t2b1", item.date),
                      th(cls := "t2b2", item.hgb_rate_01),
                      th(cls := "t2b3", item.hgb_rate_02),
                      th(cls := "t2b4", item.hgb_rate_03),
                      th(cls := "t2b5", item.hgb_rate_04),
                      th(cls := "t2b6", item.hgb_rate_05),
                      th(cls := "t2b7", item.hgb_rate_06),
                      th(cls := "t2b8", item.hgb_rate_07),
                      th(cls := "t2b9", item.hgb_rate_08),
                      th(cls := "t2b10", item.hgb_rate_09),
                      th(cls := "t2b11", item.hgb_rate_10),
                      th(cls := "t2b12", item.hgb_rate_11),
                      th(cls := "t2b13", item.hgb_rate_12)
                    )
                )
              ),
              br,
              p(b("Stand ", dateOfReport.toString("dd.MM.yyyy")))
            )
          )

      // logger.debug("Running SCALATAGS - RESULT: HTML[%s]".format(htmlString))
      logger.info("Running SCALATAGS - END")
      htmlString
    }

    def runJsoup(html: String): String =
      logger.info("Running JSOUP - BEGIN")
      val document: Document = Jsoup.parse(html)
      document.outputSettings.syntax(Document.OutputSettings.Syntax.xml)
      // logger.debug("Running JSOUP - RESULT: XHTML[%s]".format(document.html))
      logger.info("Running JSOUP - END")
      document.html()

    def runFlyingSaucer(xhtml: String, fileOfReport: FileId, basePathFromURL: String): Unit =
      logger.info("Running FLYINGSAUCER - BEGIN: FILE_OF_REPORT[%s] BASE_PATH[%s]".format(fileOfReport, basePathFromURL))
      val outputStream = fileOfReport.outputStream
      val renderer = new ITextRenderer
      renderer.setDocumentFromString(xhtml, basePathFromURL)
      renderer.layout()
      renderer.createPDF(outputStream)
      outputStream.close()
      logger.info("Running FLYINGSAUCER - END")

    imgSwap()                                                 // -- Running Image Swap
    imgSpread()                                               // -- Running Image Spread
    val htmlString = runScalatags()                           // -- Running SCALATAGS
    val xhtml: String = runJsoup(htmlString)                  // -- Running JSOUP
    val fileOfReport = FileId(filename, conf.pdf)
    val basePathFromURL = conf.template.target.path.toUri.toURL.toString
    runFlyingSaucer(xhtml, fileOfReport, basePathFromURL)     // -- Running FLYING SAUCER
  }

end Reporter
