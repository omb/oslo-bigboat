package no.omb.bigboat.data

import no.omb.bigboat.BigBoat
import java.text.DecimalFormat

data class ClubEntry(
    val club: String,
    var score: Double = 0.0,
    var numScores: Int = 0
) : Comparable<ClubEntry> {

    override fun toString(): String {
        val df = DecimalFormat("#0.00")
        return club + BigBoat.SEP + df.format(score / BigBoat.CLUB_MAX_SCORES)
    }

    override fun compareTo(other: ClubEntry): Int {
        return (score * 100 - other.score * 100).toInt()
    }
}
