package no.omb.bigboat.data

data class RaceData(
    val dataFileName: String,
    val dataFileType: DataFileType
) {
    enum class DataFileType {
        SEILMAG_CSV,
        SEILMAG_CSV2,
        SEILMAG_CSV3,
        SEILMAG_CSV4,
        ULLERN_CSV,
        DROBAK_CSV,
        M2S_CSV,
        M2S_CSV2,
        M2S_CSV3,
        M2S_CSV4,
        M2S_CSV6,
        SAILWAVE_RACE_HTML,
        SAILWAVE_SUMMARY_HTML,
        SAILWAVE_SUMMARY2_HTML
    }

    companion object {
        val races = arrayOf(
            RaceData("Bundefjorden Rundt", DataFileType.M2S_CSV4),
            RaceData("Bjørvikasprinten", DataFileType.M2S_CSV6),
            RaceData("Isbrytern", DataFileType.M2S_CSV),
            RaceData("Skagen Offshore Race", DataFileType.M2S_CSV6),
            RaceData("Oslofjorden Rundt", DataFileType.M2S_CSV3),
            RaceData("Drøbak Tristein", DataFileType.DROBAK_CSV),
            RaceData("Færderseilasen", DataFileType.M2S_CSV4),
            //RaceData("Grillcup", DataFileType.M2S_CSV3),
            //RaceData("Håøya Rundt", DataFileType.M2S_CSV3),
            RaceData("Asker Rundt", DataFileType.M2S_CSV2),
            RaceData("Hollænderseilasen", DataFileType.M2S_CSV3),
            //RaceData("Øyseilasen", DataFileType.M2S_CSV2),
            //RaceData("SætreKruset", DataFileType.SEILMAG_CSV4),
            //RaceData("Nesodden Høstcup", DataFileType.M2S_CSV4)
        )
    }
}
