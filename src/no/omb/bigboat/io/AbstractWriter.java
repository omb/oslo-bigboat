package no.omb.bigboat.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.omb.bigboat.RaceData;
import no.omb.bigboat.SeriesEntry;

public abstract class AbstractWriter {

	protected String getSeriesResultHeader() {
		StringBuilder sb = new StringBuilder("Plass;Seilnr;B�t;Skipper;B�ttype;Seilforening");
		for (RaceData race : RaceData.races) {
			sb.append(";Poeng ");
			sb.append(race.getDataFileName());
		}
		sb.append(";Poeng sammenlagt");
		return sb.toString();
	}

	protected String getRaceResultHeader() {
		return "Plass;Seilnr;B�t;Skipper;B�ttype;Forening;Plass i klasse;Poeng";
	}

	protected String getClubsResultHeader() {
		return "Plass;Seilforening;Snitt";
	}

	protected String getRegattaerString() {
		final String suffix = RaceData.races.length != 1 ? "er" : "";
		return "etter " +  RaceData.races.length + " regatta" + suffix;
	}

	@SuppressWarnings("all")
	protected String getStrykningerString() {
		final String prefix = SeriesEntry.CANCELS == 0 ? "ingen" : "" + SeriesEntry.CANCELS;
		final String suffix = SeriesEntry.CANCELS != 1 ? "er" : "";
		return prefix + " strykning" + suffix;
	}

	protected String[] addPlace(String[] line, int place) {
		List<String> newLine = new ArrayList<>();
		newLine.add(Integer.toString(place));
		newLine.addAll(Arrays.asList(line));
		String[] newArr = new String[newLine.size()];
		return newLine.toArray(newArr);
	}

}
