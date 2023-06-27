package no.omb.bigboat.io

import no.omb.bigboat.BigBoat
import java.io.FileNotFoundException
import java.io.IOException
import java.io.PrintWriter

class HtmlWriter : AbstractWriter() {
    fun writeSeriesResultHtml() {
        val fileName = BigBoat.RESULTS + "/" + "IndreOslofjordBigBoatSeries" + BigBoat.YEAR + ".html"
        val seriesList = BigBoat.sortedSeries
        val clubList = BigBoat.sortedClubs
        val writer: PrintWriter
        try {
            writer = PrintWriter(fileName, BigBoat.CHARSET)
            writeSeriesResultHtmlOpen(writer)
            writer.println("<h1>Indre Oslofjord Bigboat Cup og KM i Norrating " + BigBoat.YEAR + "</h1>")
            writer.println("<h2>Resultater sammenlagt $regattaerString, $strykningerString</h2>")
            writer.println("<table>")
            writeSeriesResultHtmlRow(writer = writer, isHeader = true, alt = false, columns = seriesResultHeader.split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            var place = 1
            for (entry in seriesList) {
                val line: Array<String> = entry.toString().split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                writeSeriesResultHtmlRow(writer, false, place % 2 == 0, addPlace(line, place))
                place++
            }
            writer.println("</table>")
            writer.println("<h2>Beste seilforening</h2>")
            writer.println("<table>")
            writeSeriesResultHtmlRow(writer = writer, isHeader = true, alt = false, columns = clubsResultHeader.split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            place = 1
            for (entry in clubList) {
                val line: Array<String> = entry.toString().split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                writeSeriesResultHtmlRow(writer, false, place % 2 == 0, addPlace(line, place))
                place++
            }
            writer.println("</table>")
            writeSeriesResultHtmlClose(writer)
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeSeriesResultHtmlRow(writer: PrintWriter, isHeader: Boolean, alt: Boolean, columns: Array<String>) {
        val elem = if (isHeader) "th" else "td"
        val css = if (alt) " class=\"alt\"" else ""
        writer.print("<tr$css>")
        for (column in columns) {
            writer.printf("<%s>%s</%s>", elem, column, elem)
        }
        writer.println("</tr>")
    }

    private fun writeSeriesResultHtmlOpen(writer: PrintWriter) {
        writer.println("<!DOCTYPE html>")
        writer.println("<html lang=\"no\">")
        writer.println("  <head>")
        writer.println("    <meta charset=\"" + BigBoat.CHARSET + "\">")
        writer.println("    <title>Indre Oslofjord Bigboat Series " + BigBoat.YEAR + "</title>")
        writer.println("    <link rel=\"stylesheet\" media=\"screen\" href=\"style.css\">")
        writer.println("    <style>")
        writer.println("      h1, h2, table {")
        writer.println("        font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;")
        writer.println("      }")
        writer.println("      table {")
        writer.println("        border-collapse: collapse;")
        writer.println("      }")
        writer.println("      td, th {")
        writer.println("        font-size: 1em;")
        writer.println("        border: 1px solid gray;")
        writer.println("        padding: 3px 7px 2px 7px;")
        writer.println("      }")
        writer.println("      th {")
        writer.println("        font-size: 1.1em;")
        writer.println("        text-align: left;")
        writer.println("        padding-top: 5px;")
        writer.println("        padding-bottom: 4px;")
        writer.println("        background-image: url(http://www.xul.fr/en/css/table-shaded.png);")
        writer.println("      }")
        writer.println("      tr.alt td, th {")
        writer.println("        color: #000000;")
        writer.println("        background-color: #E8E8FF;")
        writer.println("      }")
        writer.println("    </style>")
        writer.println("  </head>")
        writer.println("  <body>")
    }

    private fun writeSeriesResultHtmlClose(writer: PrintWriter) {
        writer.println("  </body>")
        writer.println("</html>")
    }

    companion object {
        val instance = HtmlWriter()
    }
}
