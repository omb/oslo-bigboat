package no.omb.bigboat;

public class Boat {

	private String sailNo;
	private String boatName;
	private String boatType;
	private String skipper;
	private String club;

	public Boat(String sailNo, String boatName, String boatType, String skipper, String club) {
		this.sailNo = sailNo;
		this.boatName = boatName;
		this.boatType = boatType;
		this.skipper = skipper;
		this.club = club;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boatName == null) ? 0 : boatName.hashCode());
		result = prime * result + ((sailNo == null) ? 0 : sailNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Boat other = (Boat) obj;
		if (boatName == null) {
			if (other.boatName != null)
				return false;
		} else if (!boatName.equals(other.boatName))
			return false;
		if (sailNo == null) {
			if (other.sailNo != null)
				return false;
		} else if (!sailNo.equals(other.sailNo))
			return false;
		return true;
	}

	public String getSailNo() {
		return sailNo;
	}
	public void setSailNo(String sailNo) {
		this.sailNo = sailNo;
	}
	public String getBoatName() {
		return boatName;
	}
	public void setBoatName(String boatName) {
		this.boatName = boatName;
	}
	public String getBoatType() {
		return boatType;
	}
	public void setBoatTypee(String boatType) {
		this.boatType = boatType;
	}
	public String getSkipper() {
		return skipper;
	}
	public void setSkipper(String skipper) {
		this.skipper = skipper;
	}
	public String getClub() {
		return club;
	}
	public void setClub(String club) {
		this.club = club;
	}

	public String toString() {
		return getSailNo() + BigBoat.SEP + getBoatName() + BigBoat.SEP + getSkipper() + BigBoat.SEP + getBoatType() + BigBoat.SEP + getClub();
	}

}
