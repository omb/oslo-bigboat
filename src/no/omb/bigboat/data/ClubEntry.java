package no.omb.bigboat.data;

import java.text.DecimalFormat;

import no.omb.bigboat.BigBoat;

public class ClubEntry implements Comparable<ClubEntry> {
	
	private String club;
	private double score;
	private int numScores = 0;

	public ClubEntry(String club) {
		this.club = club;
	}

	public String getClub() {
		return club;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getNumScores() {
		return numScores;
	}

	public void setNumScores(int numScores) {
		this.numScores = numScores;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#0.00");
		return club + BigBoat.SEP + df.format(score/BigBoat.CLUB_MAX_SCORES);
	}

	@Override
	public int compareTo(ClubEntry o) {
		return (int) (score*100 - o.score*100);
	}

}
