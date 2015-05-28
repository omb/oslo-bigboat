package no.omb.bigboat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class BigBoat {
	
	private static final String DATA = "data-2015";
	private static final String RESULTS = "results-2015";
	private static final String CHARSET = "ISO-8859-1";
	public static final char SEP = ';';
	public static final int CLUB_MAX_SCORES = 5;

	private static Map<String, List<RaceEntry>> raceResults = new HashMap<String, List<RaceEntry>>();
	private static Map<Boat, SeriesEntry> seriesEntries = new HashMap<Boat, SeriesEntry>();
	private static Map<String, ClubEntry> clubEntries = new HashMap<String, ClubEntry>();

	public static void main(String[] args) {
		for (String race : SeriesEntry.races) {
			List<String[]> entries = readDataFile(DATA + "/" + race + ".csv");
			raceResults.put(race, parseEntries(entries));
		}
		logRaces();
		buildSeries();
		logSeries();
		logClubs();
		writeResults();
	}

	private static List<String[]> readDataFile(String fileName) {
		CSVReader reader;
		try {
			FileInputStream is = new FileInputStream(fileName);
			reader = new CSVReader(new InputStreamReader(is, CHARSET), SEP);
			List<String[]> entries = reader.readAll();
			reader.close();
			return entries;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<RaceEntry> parseEntries(List<String[]> entries) {
		List<RaceEntry> raceEntries = new ArrayList<>();
		int classStartIndex = findClassStart(entries, 0);
		while (classStartIndex != -1) {
			int classSize = findClassSize(entries, classStartIndex);
			for (int ix = classStartIndex; ix < classStartIndex+classSize; ++ix) {
				RaceEntry newEntry = parseEntry(entries.get(ix), classSize);
				addOrUpdateRaceEntry(raceEntries, newEntry);
			}
			classStartIndex = findClassStart(entries, classStartIndex + classSize);
		}
		Collections.sort(raceEntries);
		return raceEntries;
	}
	
	private static void addOrUpdateRaceEntry(List<RaceEntry> raceEntries, RaceEntry newEntry) {
		for (RaceEntry entry : raceEntries) {
			if (entry.getBoat().equals(newEntry.getBoat())) {
				if (entry.getScore() > newEntry.getScore()) {
					entry.applyValues(newEntry);
				}
				return;
			}
		}
		raceEntries.add(newEntry);
	}

	private static RaceEntry parseEntry(String[] col, int classSize) {
		RaceEntry entry = new RaceEntry();
		try {
			entry.setPlaceNo(Integer.parseInt(col[0]));
			entry.setBoat(new Boat(col[1], col[2], col[5], col[3], col[4]));
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Error in line: " + col);
		}
		entry.setClassSize(classSize);
		entry.setScore(entry.getPlaceNo() * SeriesEntry.SCORE_FACTOR * 1.0 / classSize);
		return entry;
	}

	private static int findClassStart(List<String[]> entries, int startIndex) {
		for (int ix = startIndex; ix < entries.size(); ++ix) {
			try {
				Integer.parseInt(entries.get(ix)[0]);
				if (entries.get(ix).length <= 1) {
					continue;
				}
				return ix;
			}
			catch (NumberFormatException e) {
				// Do nothing
			}
		}
		return -1;
	}
	
	private static int findClassSize(List<String[]> entries, int classStartIndex) {
		for (int ix = classStartIndex+1; ix < entries.size(); ++ix) {
			try {
				Integer.parseInt(entries.get(ix)[0]);
				if (entries.get(ix).length <= 1) {
					return ix - classStartIndex;
				}
			}
			catch (NumberFormatException e) {
				return ix - classStartIndex;
			}
		}
		return entries.size() - classStartIndex;
	}

	private static void buildSeries() {
		for (String race : SeriesEntry.races) {
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
			if (clubEntry.getNumScores() <= CLUB_MAX_SCORES) {
				clubEntry.setScore(clubEntry.getScore() + seriesEntry.getScore());
				clubEntry.setNumScores(clubEntry.getNumScores() + 1);
			}
		}
		for (ClubEntry clubEntry : clubEntries.values()) {
			if (clubEntry.getNumScores() < CLUB_MAX_SCORES) {
				clubEntry.setScore(clubEntry.getScore() + ((CLUB_MAX_SCORES-clubEntry.getNumScores())*SeriesEntry.DEFAULT_SCORE*SeriesEntry.races.length));
			}
		}
	}

	private static List<SeriesEntry> getSortedSeries() {
		List<SeriesEntry> list = new ArrayList<>();
		list.addAll(seriesEntries.values());
		Collections.sort(list);
		return list;
	}

	private static List<ClubEntry> getSortedClubs() {
		List<ClubEntry> list = new ArrayList<>();
		list.addAll(clubEntries.values());
		Collections.sort(list);
		return list;
	}
	
	private static void logClubs() {
		List<ClubEntry> list = getSortedClubs();
		System.out.println("\nBeste seilforening\n===================");
		int place = 1;
		for (ClubEntry clubEntry : list) {
			System.out.println("" + place + SEP + clubEntry);
			++place;
		}
	}

	private static void logSeries() {
		List<SeriesEntry> list = getSortedSeries();
		int place = 1;
		System.out.println("Sammenlagt, " + SeriesEntry.CANCELS + " strykning(er)\n===========================");
		for (SeriesEntry seriesEntry : list) {
			System.out.println("" + place + SEP + seriesEntry);
			++place;
		}
	}

	private static void logRaces() {
		for (String race : SeriesEntry.races) {
			System.out.println(race + "\n===================");
			int place = 1;
			for (RaceEntry entry : raceResults.get(race)) {
				System.out.println("" + place + SEP + entry);
				++place;
			}
			System.out.println("\n");
		}
	}

	private static void writeResults() {
		for (String race : SeriesEntry.races) {
			writeRaceResultFile(race);
		}
		writeSeriesResultFile();
		writeClubsResultFile();
	}

	public static void writeRaceResultFile(String race) {
		String fileName = RESULTS + "/" + race + ".csv";
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getRaceResultHeader().split(String.valueOf(SEP)));
			int place = 1;
			for (RaceEntry entry : raceResults.get(race)) {
				String[] line = entry.toString().split(String.valueOf(SEP));
				writer.writeNext(addPlace(line, place));
				place++;
			}
			writer.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getRaceResultHeader() {
		return "Plass;Seilnr;Båt;Skipper;Båttype;Forening;Plass i klasse;Poeng";
	}

	private static void writeSeriesResultFile() {
		String fileName = RESULTS + "/" + "sammenlagt" + ".csv";
		List<SeriesEntry> list = getSortedSeries();
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getSeriesResultHeader().split(String.valueOf(SEP)));
			int place = 1;
			for (SeriesEntry entry : list) {
				String[] line = entry.toString().split(String.valueOf(SEP));
				writer.writeNext(addPlace(line, place));
				place++;
			}
			writer.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getSeriesResultHeader() {
		StringBuilder sb = new StringBuilder("Plass;Seilnr;Båt;Skipper;Båttype;Seilforening");
		for (String race : SeriesEntry.races) {
			sb.append(";Poeng ");
			sb.append(race);
		}
		sb.append(";Poeng sammenlagt");
		return sb.toString();
	}

	private static void writeClubsResultFile() {
		String fileName = RESULTS + "/" + "beste-seilforening" + ".csv";
		List<ClubEntry> list = getSortedClubs();
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getClubsResultHeader().split(String.valueOf(SEP)));
			int place = 1;
			for (ClubEntry entry : list) {
				String[] line = entry.toString().split(String.valueOf(SEP));
				writer.writeNext(addPlace(line, place));
				place++;
			}
			writer.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getClubsResultHeader() {
		return "Plass;Seilforening;Snitt";
	}

	private static String[] addPlace(String[] line, int place) {
		List<String> newLine = new ArrayList<>();
		newLine.add(Integer.toString(place));
		newLine.addAll(Arrays.asList(line));
		String[] newArr = new String[newLine.size()];
		return newLine.toArray(newArr);
	}

}
