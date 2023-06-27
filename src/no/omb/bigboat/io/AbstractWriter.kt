package no.omb.bigboat.io

import no.omb.bigboat.BigBoat
import no.omb.bigboat.data.RaceData
import java.util.*

abstract class AbstractWriter {
    protected val seriesResultHeader: String
        get() {
            val sb = StringBuilder("Plass;Seilnr;Båt;Skipper;Båttype;Seilforening")
            for (race in RaceData.races) {
                sb.append(";Poeng ")
                sb.append(race.dataFileName)
            }
            sb.append(";Poeng sammenlagt")
            return sb.toString()
        }

    protected val raceResultHeader: String
        get() = "Plass;Seilnr;Båt;Skipper;Båttype;Forening;Plass i klasse;Poeng"

    protected val clubsResultHeader: String
        get() = "Plass;Seilforening;Snitt"

    protected val regattaerString: String
        get() {
            val suffix = if (RaceData.races.size != 1) "er" else ""
            return "etter " + RaceData.races.size + " regatta" + suffix
        }

    protected val strykningerString: String
        get() {
            val prefix = if (BigBoat.CANCELS == 0) "ingen" else "" + BigBoat.CANCELS
            val suffix = if (BigBoat.CANCELS != 1) "er" else ""
            return "$prefix strykning$suffix"
        }

    protected fun addPlace(line: Array<String>, place: Int): Array<String> {
        val newLine: MutableList<String> = ArrayList()
        newLine.add(place.toString())
        newLine.addAll(listOf(*line))
        return newLine.toTypedArray()
    }
}
