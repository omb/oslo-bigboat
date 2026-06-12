package no.omb.bigboat.data

import no.omb.bigboat.BigBoat
import java.text.DecimalFormat

data class RaceEntry(
    var boat: Boat? = null,
    var placeNo: Int = 0,
    var classSize: Int = 0,
    var score: Double = 0.0
) : Comparable<RaceEntry> {
    fun applyValues(entry: RaceEntry) {
        boat = entry.boat
        classSize = entry.classSize
        placeNo = entry.placeNo
        score = entry.score
    }

    override fun toString(): String {
        val df = DecimalFormat("#0.00")
        return boat.toString() + BigBoat.SEP + placeNo + BigBoat.SEP + df.format(score)
    }

    override fun compareTo(other: RaceEntry): Int {
        return (score * 100 - other.score * 100).toInt()
    }
}
