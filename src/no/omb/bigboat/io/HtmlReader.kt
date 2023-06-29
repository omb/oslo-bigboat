package no.omb.bigboat.io

import no.omb.bigboat.BigBoat
import no.omb.bigboat.data.Boat
import no.omb.bigboat.data.RaceData
import no.omb.bigboat.data.RaceData.DataFileType
import no.omb.bigboat.data.RaceEntry
import no.omb.bigboat.data.SeriesEntry
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.*
import java.util.*

class HtmlReader : DataReader {
    private var race: RaceData? = null
    override fun parseDataFile(race: RaceData): List<RaceEntry> {
        this.race = race
        return readDataFile()
    }

    private fun readDataFile(): List<RaceEntry> {
        val input = File(fileName)
        try {
            val doc = Jsoup.parse(input, "UTF-8", "http://example.com/")
            val tables = doc.select(if (isSummary) SUMMARY_TABLE else RACE_TABLE)
            return parseEntries(tables)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    private fun parseEntries(tables: Elements): List<RaceEntry> {
        val raceEntries: MutableList<RaceEntry> = ArrayList()
        for (table in tables) {
            raceEntries.addAll(parseClassEntries(table))
        }
        raceEntries.sort()
        return raceEntries
    }

    private fun parseClassEntries(table: Element): List<RaceEntry> {
        val classEntries: MutableList<RaceEntry> = ArrayList()
        val rows = table.select(if (isSummary) SUMMARY_ROW else RACE_ROW)
        val classSize = rows.size
        for (row in rows) {
            val cols = row.select("td")
            classEntries.add(parseEntry(cols, classSize))
        }
        return classEntries
    }

    private fun parseEntry(cols: Elements, classSize: Int): RaceEntry {
        val entry = RaceEntry()
        if (isSummary2) {
            val place = cols[0].text()
            entry.placeNo = place.substring(0, place.length - 2).toInt()
            entry.boat = Boat(
                cols[2].text() + "-" + cols[3].text(),
                cols[4].text(),
                cols[5].text(),
                cols[6].text(),
                cols[7].text()
            )
        } else if (isSummary) {
            val place = cols[0].text()
            entry.placeNo = place.substring(0, place.length - 2).toInt()
            entry.boat = Boat(
                cols[2].text().replace(' ', '-'),
                cols[3].text(),
                cols[4].text(),
                cols[5].text(),
                cols[6].text()
            )
        } else {
            entry.placeNo = cols[0].text().toInt()
            entry.boat = Boat(
                cols[2].text() + "-" + cols[3].text(),
                cols[5].text(),
                cols[4].text(),
                cols[6].text(),
                cols[7].text()
            )
        }
        entry.classSize = classSize
        entry.score = entry.placeNo * SeriesEntry.SCORE_FACTOR * 1.0 / classSize
        return entry
    }

    private val fileName: String
        get() = BigBoat.DATA + "/" + race!!.dataFileName + ".html"
    private val isSummary: Boolean
        get() = race!!.dataFileType == DataFileType.SAILWAVE_SUMMARY_HTML || race!!.dataFileType == DataFileType.SAILWAVE_SUMMARY2_HTML
    private val isSummary2: Boolean
        get() = race!!.dataFileType == DataFileType.SAILWAVE_SUMMARY2_HTML

    companion object {
        val instance = HtmlReader()
        private const val RACE_TABLE = "table.racetable"
        private const val RACE_ROW = "tr.racerow"
        private const val SUMMARY_TABLE = "table.summarytable"
        private const val SUMMARY_ROW = "tr.summaryrow"
    }
}
