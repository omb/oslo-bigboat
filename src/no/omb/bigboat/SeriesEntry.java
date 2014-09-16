package no.omb.bigboat;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeriesEntry implements Comparable<SeriesEntry>{

	public static final String[] races = {
		"Isbrytern",
		"Bundefjorden Rundt",
		"Oslofjorden Rundt",
		"Håøya Rundt",
		"Hurum Golden Cup",
		"Ulabrand"};

	public static final int SCORE_FACTOR = 10;
	public static final int DEFAULT_SCORE = 12;
	public static final int CANCELS = 2;

	private Boat boat;
	private double score;
	private Map<String, RaceEntry> raceEntries = new HashMap<String, RaceEntry>();

	public SeriesEntry(Boat boat) {
		this.boat = boat;
	}

	public Boat getBoat() {
		return boat;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Map<String, RaceEntry> getRaceEntries() {
		return raceEntries;
	}

	public void computeSeriesScore() {
		double[] scores = new double[races.length];
		int ix = 0;
		for (; ix < SeriesEntry.races.length - raceEntries.size(); ++ix) {
			scores[ix] = SeriesEntry.DEFAULT_SCORE;
		}
		for (RaceEntry raceEntry : raceEntries.values()) {
			scores[ix] = raceEntry.getScore();
			++ix;
		}
		Arrays.sort(scores);
		score = 0;
		for (ix = 0; ix < SeriesEntry.races.length - SeriesEntry.CANCELS; ++ix) {			
			score = score + scores[ix];
		}
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#0.00");
		StringBuilder sb = new StringBuilder(boat.toString());
		for (String race : SeriesEntry.races) {
			sb.append(BigBoat.SEP);
			if (raceEntries.containsKey(race)) {
				sb.append(df.format(raceEntries.get(race).getScore()));
			}
			else {
				sb.append(DEFAULT_SCORE);
			}
		}
		sb.append(BigBoat.SEP);
		sb.append(df.format(score));
		return sb.toString();
	}

	@Override
	public int compareTo(SeriesEntry o) {
		return (int) (score*100 - o.score*100);
	}

}
