package no.omb.bigboat

import no.omb.bigboat.data.*
import no.omb.bigboat.data.RaceData.DataFileType
import no.omb.bigboat.io.*
import java.io.File
import java.util.*


object BigBoat {
    const val YEAR = "2023"
    const val DATA = "data-$YEAR"
    const val RESULTS = "results-$YEAR"
    const val CHARSET = "UTF-8"
    const val SEP = ';'
    const val CLUB_MAX_SCORES = 5
    const val CANCELS = 2
    private val raceResults: MutableMap<RaceData, List<RaceEntry>> = HashMap()
    private val seriesEntries: MutableMap<Boat, SeriesEntry> = HashMap()
    private val clubEntries: MutableMap<String, ClubEntry> = HashMap()

    @JvmStatic
    fun main(args: Array<String>) {
        for (race in RaceData.races) {
            val raceEntries = getDataReader(race).parseDataFile(race)
            raceResults[race] = raceEntries
        }
        buildSeries()
        logResults()
        writeResults()
    }

    private fun buildSeries() {
        for (race in RaceData.races) {
            for (entry in raceResults[race]!!) {
                val boat = entry.boat!!
                var seriesEntry = seriesEntries[boat]
                if (seriesEntry == null) {
                    seriesEntry = SeriesEntry(boat)
                    seriesEntries[boat] = seriesEntry
                }
                seriesEntry.raceEntries[race] = entry
            }
        }
        for (seriesEntry in seriesEntries.values) {
            seriesEntry.computeSeriesScore()
        }
        computeClubScores()
    }

    private fun computeClubScores() {
        val list = sortedSeries
        for (seriesEntry in list) {
            val club = seriesEntry.boat.club
            var clubEntry = clubEntries[club]
            if (clubEntry == null) {
                clubEntry = ClubEntry(club)
                clubEntries[club] = clubEntry
            }
            if (clubEntry.numScores < CLUB_MAX_SCORES) {
                clubEntry.score = clubEntry.score + seriesEntry.score
                clubEntry.numScores = clubEntry.numScores + 1
            }
        }
        for (clubEntry in clubEntries.values) {
            if (clubEntry.numScores < CLUB_MAX_SCORES) {
                clubEntry.score =
                    clubEntry.score + (CLUB_MAX_SCORES - clubEntry.numScores) * SeriesEntry.DEFAULT_SCORE * RaceData.races.size
            }
        }
    }

    fun getRaceResults(): Map<RaceData, List<RaceEntry>> {
        return raceResults
    }

    val sortedSeries: List<SeriesEntry>
        get() {
            val list: MutableList<SeriesEntry> = ArrayList()
            list.addAll(seriesEntries.values)
            list.sort()
            return list
        }
    val sortedClubs: List<ClubEntry>
        get() {
            val list: MutableList<ClubEntry> = ArrayList()
            list.addAll(clubEntries.values)
            list.sort()
            return list
        }

    private fun logResults() {
        ConsoleWriter.instance.logRaces()
        ConsoleWriter.instance.logSeries()
        ConsoleWriter.instance.logClubs()
        ConsoleWriter.instance.logDuplicates()
    }

    private fun writeResults() {
        File(RESULTS).mkdir()
        for (race in RaceData.races) {
            CsvWriter.instance.writeRaceResultFile(race)
        }
        HtmlWriter.instance.writeSeriesResultHtml()
        CsvWriter.instance.writeSeriesResultFile()
        CsvWriter.instance.writeClubsResultFile()
    }

    private fun getDataReader(race: RaceData): DataReader {
        return when (race.dataFileType) {
            DataFileType.SAILWAVE_RACE_HTML, DataFileType.SAILWAVE_SUMMARY_HTML, DataFileType.SAILWAVE_SUMMARY2_HTML -> HtmlReader.instance
            DataFileType.SEILMAG_CSV, DataFileType.SEILMAG_CSV2, DataFileType.SEILMAG_CSV3, DataFileType.SEILMAG_CSV4, DataFileType.ULLERN_CSV, DataFileType.M2S_CSV, DataFileType.M2S_CSV2, DataFileType.M2S_CSV3 -> CsvReader.instance
        }
    }
}
