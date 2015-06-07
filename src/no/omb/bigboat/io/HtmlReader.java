package no.omb.bigboat.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.RaceData;
import no.omb.bigboat.RaceEntry;

public class HtmlReader implements DataReader {

	private static HtmlReader instance = new HtmlReader();

	@Override
	public List<RaceEntry> parseDataFile(RaceData race) {
		List<String[]> entries = readDataFile(getFileName(race));
		return parseEntries(entries);
	}

	private List<String[]> readDataFile(String fileName) {
		File input = new File(fileName);
		try {
			Document doc = Jsoup.parse(input, BigBoat.CHARSET, "http://example.com/");
			Elements tables = doc.select("table.racetable");
			for (Element table : tables) {
				Elements rows = table.select("tr.racerow");
				for (Element row : rows) {
					Elements cols = row.select("td");
					System.out.println("Plass: " + cols.get(0) + " Båt: " + cols.get(2) + "-" + cols.get(3) + " Skipper: " + cols.get(6) + " Seilforening: " + cols.get(7));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<RaceEntry> parseEntries(List<String[]> entries) {
		return null;
	}

	public static HtmlReader getInstance() {
		return instance;
	}

	private String getFileName(RaceData race) {
		return BigBoat.DATA + "/" + race.getDataFileName() + ".html";
	}

}
