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
import no.omb.bigboat.data.Boat;
import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceData.DataFileType;
import no.omb.bigboat.data.RaceEntry;
import no.omb.bigboat.data.SeriesEntry;

public class HtmlReader implements DataReader {

	private static final HtmlReader instance = new HtmlReader();

	private static final String RACE_TABLE = "table.racetable";
	private static final String RACE_ROW = "tr.racerow";
	private static final String SUMMARY_TABLE = "table.summarytable";
	private static final String SUMMARY_ROW = "tr.summaryrow";

	private RaceData race;

	@Override
	public List<RaceEntry> parseDataFile(RaceData race) {
		this.race = race;
		return readDataFile();
	}

	private List<RaceEntry> readDataFile() {
		File input = new File(getFileName());
		try {
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			Elements tables = doc.select(isSummary() ? SUMMARY_TABLE : RACE_TABLE);
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
		Elements rows = table.select(isSummary() ? SUMMARY_ROW : RACE_ROW);
		int classSize = rows.size();
		for (Element row : rows) {
			Elements cols = row.select("td");
			classEntries.add(parseEntry(cols, classSize));
		}		
		return classEntries;		
	}

	private RaceEntry parseEntry(Elements cols, int classSize) {
		RaceEntry entry = new RaceEntry();
		if (isSummary2()) {
			String place = cols.get(0).text();
			entry.setPlaceNo(Integer.parseInt(place.substring(0, place.length()-2)));
			entry.setBoat(new Boat(cols.get(2).text() + "-" + cols.get(3).text(),
					cols.get(4).text(),
					cols.get(5).text(),
					cols.get(6).text(),
					cols.get(7).text()));
		}
		else if (isSummary()) {
			String place = cols.get(0).text();
			entry.setPlaceNo(Integer.parseInt(place.substring(0, place.length()-2)));
			entry.setBoat(new Boat(cols.get(2).text().replace(' ', '-'),
					cols.get(3).text(),
					cols.get(4).text(),
					cols.get(5).text(),
					cols.get(6).text()));
		}
		else {
			entry.setPlaceNo(Integer.parseInt(cols.get(0).text()));
			entry.setBoat(new Boat(cols.get(2).text() + "-" + cols.get(3).text(),
					cols.get(5).text(),
					cols.get(4).text(),
					cols.get(6).text(),
					cols.get(7).text()));
		}
		entry.setClassSize(classSize);
		entry.setScore(entry.getPlaceNo() * SeriesEntry.SCORE_FACTOR * 1.0 / classSize);
		return entry;
	}

	public static HtmlReader getInstance() {
		return instance;
	}

	private String getFileName() {
		return BigBoat.DATA + "/" + race.getDataFileName() + ".html";
	}

	private boolean isSummary() {
		return race.getDataFileType() == DataFileType.SAILWAVE_SUMMARY_HTML || race.getDataFileType() == DataFileType.SAILWAVE_SUMMARY2_HTML;
	}

	private boolean isSummary2() {
		return race.getDataFileType() == DataFileType.SAILWAVE_SUMMARY2_HTML;
	}
	
}
