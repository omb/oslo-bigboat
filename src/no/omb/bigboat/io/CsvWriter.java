package no.omb.bigboat.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.data.ClubEntry;
import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceEntry;
import no.omb.bigboat.data.SeriesEntry;
import au.com.bytecode.opencsv.CSVWriter;

public class CsvWriter extends AbstractWriter {

	private static CsvWriter instance = new CsvWriter();

	public void writeRaceResultFile(RaceData race) {
		String fileName = BigBoat.RESULTS + "/" + race.getDataFileName() + ".csv";
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getRaceResultHeader().split(String.valueOf(BigBoat.SEP)));
			int place = 1;
			for (RaceEntry entry : BigBoat.getRaceResults().get(race)) {
				String[] line = entry.toString().split(String.valueOf(BigBoat.SEP));
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
	
	public void writeSeriesResultFile() {
		String fileName = BigBoat.RESULTS + "/" + "sammenlagt" + ".csv";
		List<SeriesEntry> list = BigBoat.getSortedSeries();
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getSeriesResultHeader().split(String.valueOf(BigBoat.SEP)));
			int place = 1;
			for (SeriesEntry entry : list) {
				String[] line = entry.toString().split(String.valueOf(BigBoat.SEP));
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

	public void writeClubsResultFile() {
		String fileName = BigBoat.RESULTS + "/" + "beste-seilforening" + ".csv";
		List<ClubEntry> list = BigBoat.getSortedClubs();
		CSVWriter writer;
		try {
			FileOutputStream os = new FileOutputStream(fileName);
			writer = new CSVWriter(new OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeNext(getClubsResultHeader().split(String.valueOf(BigBoat.SEP)));
			int place = 1;
			for (ClubEntry entry : list) {
				String[] line = entry.toString().split(String.valueOf(BigBoat.SEP));
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

	public static CsvWriter getInstance() {
		return instance;
	}

}
