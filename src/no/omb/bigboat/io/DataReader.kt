package no.omb.bigboat.io

import no.omb.bigboat.data.RaceData
import no.omb.bigboat.data.RaceEntry

interface DataReader {
    fun parseDataFile(race: RaceData): List<RaceEntry>
}
