package no.omb.bigboat.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.data.Boat;
import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceEntry;
import no.omb.bigboat.data.SeriesEntry;
import au.com.bytecode.opencsv.CSVReader;

public class CsvReader implements DataReader {

	private static CsvReader instance = new CsvReader();

	@Override
	public List<RaceEntry> parseDataFile(RaceData race) {
		List<String[]> entries = readDataFile(getFileName(race));
		return parseEntries(entries);
	}

	private List<String[]> readDataFile(String fileName) {
		CSVReader reader;
		try {
			FileInputStream is = new FileInputStream(fileName);
			reader = new CSVReader(new InputStreamReader(is, BigBoat.CHARSET), BigBoat.SEP);
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

	private List<RaceEntry> parseEntries(List<String[]> entries) {
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

	private void addOrUpdateRaceEntry(List<RaceEntry> raceEntries, RaceEntry newEntry) {
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

	private RaceEntry parseEntry(String[] col, int classSize) {
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

	private int findClassStart(List<String[]> entries, int startIndex) {
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

	private int findClassSize(List<String[]> entries, int classStartIndex) {
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

	private String getFileName(RaceData race) {
		return BigBoat.DATA + "/" + race.getDataFileName() + ".csv";
	}

	public static CsvReader getInstance() {
		return instance;
	}

}