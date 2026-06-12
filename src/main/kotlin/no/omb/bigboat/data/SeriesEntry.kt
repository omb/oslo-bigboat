package no.omb.bigboat.data

import no.omb.bigboat.BigBoat
import java.text.DecimalFormat
import java.util.*

data class SeriesEntry(val boat: Boat) : Comparable<SeriesEntry> {
    var score = 0.0
    val raceEntries: MutableMap<RaceData, RaceEntry> = HashMap()

    fun computeSeriesScore() {
        val scores = DoubleArray(RaceData.races.size)
        var ix = 0
        while (ix < RaceData.races.size - raceEntries.size) {
            scores[ix] = DEFAULT_SCORE.toDouble()
            ++ix
        }
        for (raceEntry in raceEntries.values) {
            scores[ix] = raceEntry.score
            ++ix
        }
        Arrays.sort(scores)
        score = 0.0
        ix = 0
        while (ix < RaceData.races.size - BigBoat.CANCELS) {
            score += scores[ix]
            ++ix
        }
    }

    override fun toString(): String {
        val df = DecimalFormat("#0.00")
        val sb = StringBuilder(boat.toString())
        for (race in RaceData.races) {
            sb.append(BigBoat.SEP)
            raceEntries[race]?.let { sb.append(df.format(it.score)) } ?: sb.append(DEFAULT_SCORE)
        }
        sb.append(BigBoat.SEP)
        sb.append(df.format(score))
        return sb.toString()
    }

    override fun compareTo(other: SeriesEntry): Int {
        return (score * 100 - other.score * 100).toInt()
    }

    companion object {
        const val SCORE_FACTOR = 10
        const val DEFAULT_SCORE = 12
    }
}
