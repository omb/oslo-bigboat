package no.omb.bigboat.data;

public class RaceData {

	public static final RaceData[] races = {
		new RaceData("Isbrytern", DataFileType.SEILMAG_CSV),
		new RaceData("Oslofjorden Rundt", DataFileType.SEILMAG_CSV),
		new RaceData("Bundefjorden Rundt", DataFileType.SAILWAVE_RACE_HTML),
		new RaceData("Håøya Rundt", DataFileType.SEILMAG_CSV),
		new RaceData("Hurum Golden Cup", DataFileType.SEILMAG_CSV),
		new RaceData("Nesodden Høstcup", DataFileType.SAILWAVE_SUMMARY_HTML),
	};

	public static final String[] all_races = {
		"Isbrytern",
		"Oslofjorden Rundt",
		"Bundefjorden Rundt",
		"Håøya Rundt",
		"Hurum Golden Cup",
		"Ulabrand",
		"Nesodden Høstcup"};	

	public enum DataFileType {
		SEILMAG_CSV,
		SAILWAVE_RACE_HTML,
		SAILWAVE_SUMMARY_HTML
	}

	private DataFileType dataFileType;
	private String dataFileName;

	public RaceData(String dataFileName, DataFileType dataFileType) {
		this.dataFileName = dataFileName;
		this.dataFileType = dataFileType;
	}

	public DataFileType getDataFileType() {
		return dataFileType;
	}

	public void setDataFileType(DataFileType dataFileType) {
		this.dataFileType = dataFileType;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

}
