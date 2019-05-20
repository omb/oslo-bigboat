package no.omb.bigboat.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.data.ClubEntry;
import no.omb.bigboat.data.SeriesEntry;

public class HtmlWriter extends AbstractWriter {

	private static HtmlWriter instance = new HtmlWriter();

	public void writeSeriesResultHtml() {
		String fileName = BigBoat.RESULTS + "/" + "IndreOslofjordBigBoatSeries" + BigBoat.YEAR + ".html";
		List<SeriesEntry> seriesList = BigBoat.getSortedSeries();
		List<ClubEntry> clubList = BigBoat.getSortedClubs();
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, BigBoat.CHARSET);
			writeSeriesResultHtmlOpen(writer);
			writer.println("<h1>Indre Oslofjord Bigboat Series " + BigBoat.YEAR + "</h1>");
			writer.println("<h2>Resultater sammenlagt " + getRegattaerString() + ", " + getStrykningerString() + "</h2>");
			writer.println("<table>");
			writeSeriesResultHtmlRow(writer, true, false, getSeriesResultHeader().split(String.valueOf(BigBoat.SEP)));
			int place = 1;
			for (SeriesEntry entry : seriesList) {
				String[] line = entry.toString().split(String.valueOf(BigBoat.SEP));
				writeSeriesResultHtmlRow(writer, false, place%2==0, addPlace(line, place));
				place++;
			}
			writer.println("</table>");
			writer.println("<h2>Beste seilforening</h2>");
			writer.println("<table>");
			writeSeriesResultHtmlRow(writer, true, false, getClubsResultHeader().split(String.valueOf(BigBoat.SEP)));
			place = 1;
			for (ClubEntry entry : clubList) {
				String[] line = entry.toString().split(String.valueOf(BigBoat.SEP));
				writeSeriesResultHtmlRow(writer, false, place%2==0, addPlace(line, place));
				place++;
			}
			writer.println("</table>");
			writeSeriesResultHtmlClose(writer);
			writer.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeSeriesResultHtmlRow(PrintWriter writer, boolean isHeader, boolean alt, String[] columns) {
		final String elem = isHeader ? "th" : "td";
		final String css = alt ? " class=\"alt\"" : "";
		writer.print("<tr" + css +">");
		for (String column : columns) {
			writer.printf("<%s>%s</%s>", elem, column, elem);
		}
		writer.println("</tr>");
	}

	private void writeSeriesResultHtmlOpen(PrintWriter writer) {
		writer.println("<!DOCTYPE html>");
		writer.println("<html lang=\"no\">");
		writer.println("  <head>");
		writer.println("    <meta charset=\"" + BigBoat.CHARSET + "\">");
		writer.println("    <title>Indre Oslofjord Bigboat Series " + BigBoat.YEAR + "</title>");
		writer.println("    <link rel=\"stylesheet\" media=\"screen\" href=\"style.css\">");
		writer.println("    <style>");
		writer.println("      h1, h2, table {");
		writer.println("        font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;");
		writer.println("      }");
		writer.println("      table {");
		writer.println("        border-collapse: collapse;");
		writer.println("      }");
		writer.println("      td, th {");
		writer.println("        font-size: 1em;");
		writer.println("        border: 1px solid gray;");
		writer.println("        padding: 3px 7px 2px 7px;");
		writer.println("      }");
		writer.println("      th {");
		writer.println("        font-size: 1.1em;");
		writer.println("        text-align: left;");
		writer.println("        padding-top: 5px;");
		writer.println("        padding-bottom: 4px;");
		writer.println("        background-image: url(http://www.xul.fr/en/css/table-shaded.png);");
		writer.println("      }");
		writer.println("      tr.alt td, th {");
		writer.println("        color: #000000;");
		writer.println("        background-color: #E8E8FF;");
		writer.println("      }");
		writer.println("    </style>");
		writer.println("  </head>");
		writer.println("  <body>");
	}

	private void writeSeriesResultHtmlClose(PrintWriter writer) {
		writer.println("  </body>");
		writer.println("</html>");
	}

	public static HtmlWriter getInstance() {
		return instance;
	}

}
