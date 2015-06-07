package no.omb.bigboat.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.Boat;
import no.omb.bigboat.RaceData;
import no.omb.bigboat.RaceEntry;
import no.omb.bigboat.SeriesEntry;

public class HtmlReader implements DataReader {

	private static HtmlReader instance = new HtmlReader();

	@Override
	public List<RaceEntry> parseDataFile(RaceData race) {
		return readDataFile(getFileName(race));
	}

	private List<RaceEntry> readDataFile(String fileName) {
		File input = new File(fileName);
		try {
			Document doc = Jsoup.parse(input, BigBoat.CHARSET, "http://example.com/");
			Elements tables = doc.select("table.racetable");
			return parseEntries(tables);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<RaceEntry> parseEntries(Elements tables) {
		List<RaceEntry> raceEntries = new ArrayList<>();
		for (Element table : tables) {
			raceEntries.addAll(parseClassEntries(table));
		}
		Collections.sort(raceEntries);
		return raceEntries;
	}

	private List<RaceEntry> parseClassEntries(Element table) {
		List<RaceEntry> classEntries = new ArrayList<>();
		Elements rows = table.select("tr.racerow");
		int classSize = rows.size();
		for (Element row : rows) {
			Elements cols = row.select("td");
			classEntries.add(parseEntry(cols, classSize));
		}		
		return classEntries;		
	}

	private RaceEntry parseEntry(Elements cols, int classSize) {
		RaceEntry entry = new RaceEntry();
		try {
			entry.setPlaceNo(Integer.parseInt(cols.get(0).text()));
			entry.setBoat(new Boat(cols.get(2).text() + "-" + cols.get(3).text(),
					cols.get(5).text(),
					cols.get(4).text(),
					cols.get(6).text(),
					cols.get(7).text()));
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error in line: " + cols);
		}
		entry.setClassSize(classSize);
		entry.setScore(entry.getPlaceNo() * SeriesEntry.SCORE_FACTOR * 1.0 / classSize);
		return entry;
	}

	public static HtmlReader getInstance() {
		return instance;
	}

	private String getFileName(RaceData race) {
		return BigBoat.DATA + "/" + race.getDataFileName() + ".html";
	}

}
