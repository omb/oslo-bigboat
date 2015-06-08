package no.omb.bigboat.data;

import java.text.DecimalFormat;

import no.omb.bigboat.BigBoat;

public class RaceEntry implements Comparable<RaceEntry> {

	private Boat boat;
	private int placeNo;
	private int classSize;
	private double score;

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public int getPlaceNo() {
		return placeNo;
	}

	public void setPlaceNo(int placeNo) {
		this.placeNo = placeNo;
	}

	public int getClassSize() {
		return classSize;
	}

	public void setClassSize(int classSize) {
		this.classSize = classSize;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void applyValues(RaceEntry entry) {
		this.boat = entry.getBoat();
		this.classSize = entry.getClassSize();
		this.placeNo = entry.getPlaceNo();
		this.score = entry.getScore();
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return boat.toString() + BigBoat.SEP + placeNo + BigBoat.SEP + df.format(score);
	}

	@Override
	public int compareTo(RaceEntry o) {
		return (int) (score*100 - o.score*100);
	}
}
