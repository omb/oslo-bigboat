package no.omb.bigboat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.omb.bigboat.data.Boat;
import no.omb.bigboat.data.ClubEntry;
import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceEntry;
import no.omb.bigboat.data.SeriesEntry;
import no.omb.bigboat.io.ConsoleWriter;
import no.omb.bigboat.io.CsvReader;
import no.omb.bigboat.io.CsvWriter;
import no.omb.bigboat.io.DataReader;
import no.omb.bigboat.io.HtmlReader;
import no.omb.bigboat.io.HtmlWriter;

public class BigBoat {
	
	public static final String YEAR = "2015";
	public static final String DATA = "data-" + YEAR;
	public static final String RESULTS = "results-" + YEAR;
	public static final String CHARSET = "ISO-8859-1";
	public static final char SEP = ';';
	public static final int CLUB_MAX_SCORES = 5;

	private static Map<RaceData, List<RaceEntry>> raceResults = new HashMap<>();
	private static Map<Boat, SeriesEntry> seriesEntries = new HashMap<>();
	private static Map<String, ClubEntry> clubEntries = new HashMap<>();

	public static void main(String[] args) {
		for (RaceData race : RaceData.races) {
			List<RaceEntry> raceEntries = getDataReader(race).parseDataFile(race);
			if (raceEntries != null) {
				raceResults.put(race, raceEntries);
			}
		}
		buildSeries();
		logResults();
		writeResults();
	}

	private static void buildSeries() {
		for (RaceData race : RaceData.races) {
			for (RaceEntry entry : raceResults.get(race)) {
				Boat boat = entry.getBoat();
				SeriesEntry seriesEntry = seriesEntries.get(boat);
				if (seriesEntry == null) {
					seriesEntry = new SeriesEntry(boat);
					seriesEntries.put(boat, seriesEntry);
				}
				seriesEntry.getRaceEntries().put(race, entry);
			}
		}
		for (SeriesEntry seriesEntry : seriesEntries.values()) {
			seriesEntry.computeSeriesScore();
		}
		computeClubScores();
	}

	private static void computeClubScores() {
		List<SeriesEntry> list = getSortedSeries();
		for (SeriesEntry seriesEntry : list) {			
			String club = seriesEntry.getBoat().getClub();
			ClubEntry clubEntry = clubEntries.get(club);
			if (clubEntry == null) {
				clubEntry = new ClubEntry(club);
				clubEntries.put(club, clubEntry);
			}
			if (clubEntry.getNumScores() < CLUB_MAX_SCORES) {
				clubEntry.setScore(clubEntry.getScore() + seriesEntry.getScore());
				clubEntry.setNumScores(clubEntry.getNumScores() + 1);
			}
		}
		for (ClubEntry clubEntry : clubEntries.values()) {
			if (clubEntry.getNumScores() < CLUB_MAX_SCORES) {
				clubEntry.setScore(clubEntry.getScore() + ((CLUB_MAX_SCORES-clubEntry.getNumScores())*SeriesEntry.DEFAULT_SCORE*RaceData.races.length));
			}
		}
	}

	public static Map<RaceData, List<RaceEntry>> getRaceResults() {
		return raceResults;
	}

	public static List<SeriesEntry> getSortedSeries() {
		List<SeriesEntry> list = new ArrayList<>();
		list.addAll(seriesEntries.values());
		Collections.sort(list);
		return list;
	}

	public static List<ClubEntry> getSortedClubs() {
		List<ClubEntry> list = new ArrayList<>();
		list.addAll(clubEntries.values());
		Collections.sort(list);
		return list;
	}

	private static void logResults() {
		ConsoleWriter.getInstance().logRaces();
		ConsoleWriter.getInstance().logSeries();
		ConsoleWriter.getInstance().logClubs();
		ConsoleWriter.getInstance().logDuplicates();
	}

	private static void writeResults() {
		for (RaceData race : RaceData.races) {
			CsvWriter.getInstance().writeRaceResultFile(race);
		}
		HtmlWriter.getInstance().writeSeriesResultHtml();
		CsvWriter.getInstance().writeSeriesResultFile();
		CsvWriter.getInstance().writeClubsResultFile();
	}

	private static DataReader getDataReader(RaceData race) {
		switch (race.getDataFileType()) {
		case SAILWAVE_RACE_HTML:
		case SAILWAVE_SUMMARY_HTML:
			return HtmlReader.getInstance();
		case SEILMAG_CSV:
			return CsvReader.getInstance();
		}
		return null;
	}

}

