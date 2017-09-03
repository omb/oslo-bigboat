package no.omb.bigboat.data;

public class RaceData {

	public static final RaceData[] races = {
		new RaceData("Isbrytern", DataFileType.SEILMAG_CSV),
		new RaceData("Bjørvikasprinten", DataFileType.SEILMAG_CSV),
		new RaceData("Bundefjorden Rundt", DataFileType.SEILMAG_CSV3),
		new RaceData("Oslofjorden Rundt", DataFileType.SEILMAG_CSV3),
		new RaceData("Håøya Rundt", DataFileType.SEILMAG_CSV3),
		new RaceData("SætreKruset", DataFileType.SEILMAG_CSV3),
//		new RaceData("UllernCupen Familie SH regatta 1", DataFileType.ULLERN_CSV),
//		new RaceData("UllernCupen Familie SH regatta 2", DataFileType.ULLERN_CSV),
//		new RaceData("Nesodden Høstcup", DataFileType.SEILMAG_CSV2),
//		new RaceData("Hurum Golden Cup", DataFileType.SEILMAG_CSV),
	};

	public enum DataFileType {
		SEILMAG_CSV,
		SEILMAG_CSV2,
		SEILMAG_CSV3,
		ULLERN_CSV,
		SAILWAVE_RACE_HTML,
		SAILWAVE_SUMMARY_HTML,
		SAILWAVE_SUMMARY2_HTML
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
