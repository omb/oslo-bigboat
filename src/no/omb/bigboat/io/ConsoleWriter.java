package no.omb.bigboat.io;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.omb.bigboat.BigBoat;
import no.omb.bigboat.data.ClubEntry;
import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceEntry;
import no.omb.bigboat.data.SeriesEntry;

public class ConsoleWriter extends AbstractWriter {

	private static ConsoleWriter instance = new ConsoleWriter();

	public void logClubs() {
		List<ClubEntry> list = BigBoat.getSortedClubs();
		System.out.println("\nBeste seilforening\n===================");
		int place = 1;
		for (ClubEntry clubEntry : list) {
			System.out.println("" + place + BigBoat.SEP + clubEntry);
			++place;
		}
	}

	public void logDuplicates() {
		Set<String> sailNos = new HashSet<>();
		List<SeriesEntry> list = BigBoat.getSortedSeries();
		System.out.println("\nDuplikater\n===========================");
		for (SeriesEntry seriesEntry : list) {
			if (sailNos.contains(seriesEntry.getBoat().getSailNo())) {				
				System.out.println(seriesEntry.getBoat());
			}
			sailNos.add(seriesEntry.getBoat().getSailNo());
		}
	}

	public void logSeries() {
		List<SeriesEntry> list = BigBoat.getSortedSeries();
		int place = 1;
		System.out.println("Sammenlagt, " + getStrykningerString() + "\n===========================");
		for (SeriesEntry seriesEntry : list) {
			System.out.println("" + place + BigBoat.SEP + seriesEntry);
			++place;
		}
	}

	public void logRaces() {
		for (RaceData race : RaceData.races) {
			System.out.println(race.getDataFileName() + "\n===================");
			int place = 1;
			for (RaceEntry entry : BigBoat.getRaceResults().get(race)) {
				System.out.println("" + place + BigBoat.SEP + entry);
				++place;
			}
			System.out.println("\n");
		}
	}

	public static ConsoleWriter getInstance() {
		return instance;
	}

}
