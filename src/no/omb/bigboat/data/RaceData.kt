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
        M2S_CSV,
        M2S_CSV2,
        M2S_CSV3,
        SAILWAVE_RACE_HTML,
        SAILWAVE_SUMMARY_HTML,
        SAILWAVE_SUMMARY2_HTML
    }

    companion object {
        val races = arrayOf(
                //RaceData("Bjørvikasprinten", DataFileType.M2S_CSV),
                RaceData("Isbrytern", DataFileType.M2S_CSV),
                RaceData("Oslofjorden Rundt", DataFileType.M2S_CSV3),
                RaceData("Bundefjorden Vårcup", DataFileType.M2S_CSV3),
                RaceData("Færderseilasen", DataFileType.M2S_CSV3),
                //RaceData("Øyseilasen", DataFileType.M2S_CSV2),
                //RaceData("Håøya Rundt", DataFileType.M2S_CSV2),
                //RaceData("SætreKruset", DataFileType.SEILMAG_CSV4),
                //RaceData("Nesodden Høstcup", DataFileType.M2S_CSV3)
        )
    }
}
