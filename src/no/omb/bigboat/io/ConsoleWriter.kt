package no.omb.bigboat.io

import no.omb.bigboat.BigBoat
import no.omb.bigboat.data.RaceData

class ConsoleWriter : AbstractWriter() {
    fun logClubs() {
        val list = BigBoat.sortedClubs
        println("\nBeste seilforening\n===================")
        var place = 1
        for (clubEntry in list) {
            println("" + place + BigBoat.SEP + clubEntry)
            ++place
        }
    }

    fun logDuplicates() {
        val sailNos: MutableSet<String> = HashSet()
        val list = BigBoat.sortedSeries
        println("\nDuplikater\n===========================")
        for (seriesEntry in list) {
            if (sailNos.contains(seriesEntry.boat.sailNo)) {
                println(seriesEntry.boat)
            }
            sailNos.add(seriesEntry.boat.sailNo)
        }
    }

    fun logSeries() {
        val list = BigBoat.sortedSeries
        var place = 1
        println(
            """
    Sammenlagt, $strykningerString
    ===========================
    """.trimIndent()
        )
        for (seriesEntry in list) {
            println("" + place + BigBoat.SEP + seriesEntry)
            ++place
        }
    }

    fun logRaces() {
        for (race in RaceData.races) {
            println(race.dataFileName + "\n===================")
            var place = 1
            for (entry in BigBoat.getRaceResults()[race]!!) {
                println("" + place + BigBoat.SEP + entry)
                ++place
            }
            println("\n")
        }
    }

    companion object {
        val instance = ConsoleWriter()
    }
}
