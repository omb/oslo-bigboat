package no.omb.bigboat.data

import no.omb.bigboat.BigBoat
import java.util.*

data class Boat(
        val sailNo: String,
        val boatName: String,
        val boatType: String,
        val skipper: String,
        val club: String
) {
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = (prime * result
                + boatName.uppercase(Locale.getDefault()).hashCode())
        result = prime * result + sailNo.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val (sailNo1, boatName1) = other as Boat

        if (!boatName.equals(boatName1, ignoreCase = true)) return false
        return sailNo == sailNo1
    }

    override fun toString(): String {
        return sailNo + BigBoat.SEP + boatName + BigBoat.SEP + skipper + BigBoat.SEP + boatType + BigBoat.SEP + club
    }
}
