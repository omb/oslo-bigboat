package no.omb.bigboat.io;

import java.util.List;

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
