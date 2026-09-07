package no.omb.bigboat.io

import au.com.bytecode.opencsv.CSVReader
import no.omb.bigboat.BigBoat
import no.omb.bigboat.data.Boat
import no.omb.bigboat.data.RaceData
import no.omb.bigboat.data.RaceData.DataFileType
import no.omb.bigboat.data.RaceEntry
import no.omb.bigboat.data.SeriesEntry
import java.io.*
import java.util.*

class CsvReader : DataReader {
    private var race: RaceData? = null
    override fun parseDataFile(race: RaceData): List<RaceEntry> {
        this.race = race
        val entries = readDataFile(getFileName(race))
        return parseEntries(entries)
    }

    private fun readDataFile(fileName: String): List<Array<String>> {
        val reader: CSVReader
        try {
            val `is` = FileInputStream(fileName)
            reader = CSVReader(InputStreamReader(`is`, "UTF-8"), BigBoat.SEP)
            val entries = reader.readAll()
            reader.close()
            return entries
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    private fun parseEntries(entries: List<Array<String>>): List<RaceEntry> {
        val raceEntries: MutableList<RaceEntry> = ArrayList()
        var classStartIndex = findClassStart(entries, 0)
        while (classStartIndex != -1) {
            val classSize = findClassSize(entries, classStartIndex)
            for (ix in classStartIndex until classStartIndex + classSize) {
                val newEntry = parseEntry(entries[ix], classSize)
                addOrUpdateRaceEntry(raceEntries, newEntry)
            }
            classStartIndex = findClassStart(entries, classStartIndex + classSize)
        }
        raceEntries.sort()
        return raceEntries
    }

    private fun addOrUpdateRaceEntry(raceEntries: MutableList<RaceEntry>, newEntry: RaceEntry) {
        for (entry in raceEntries) {
            if (entry.boat == newEntry.boat) {
                if (entry.score > newEntry.score) {
                    entry.applyValues(newEntry)
                }
                return
            }
        }
        raceEntries.add(newEntry)
    }

    private fun parseEntry(col: Array<String>, classSize: Int): RaceEntry {
        val entry = RaceEntry()
        try {
            when (race!!.dataFileType) {
                DataFileType.SEILMAG_CSV4 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(
                        col[1],
                        col[2],
                        col[3],
                        col[4].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
                        col[4].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    )
                }

                DataFileType.SEILMAG_CSV3 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(
                        col[1],
                        col[2],
                        col[3],
                        col[8].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
                        col[8].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    )
                }

                DataFileType.SEILMAG_CSV2 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(
                        col[1],
                        col[2],
                        col[3],
                        col[9].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0],
                        col[9].split(" \\(".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    )
                }

                // 0: Place, 1: Sail no, 2: Boat name, 3: Boat type, 4: Skipper, 5: Club
                DataFileType.M2S_CSV -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[2], col[3], col[4], col[5])
                }

                // 0: Place, 1: Sail no, 2: Boat name, 3: Skipper, 4: Club, 5: Boat type
                DataFileType.M2S_CSV2 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[2], col[5], col[3], col[4])
                }

                // 0: Place, 1: Sail no, 2: Skipper, 3: Club, 4: Boat type, 5: Boat name
                DataFileType.M2S_CSV3 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[5], col[4], col[2], col[3])
                }

                // 0: Place, 1: Sail no, 2: Skipper, 3: Boat name, 4: Club, 5: Boat type
                DataFileType.M2S_CSV4 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[3], col[5], col[2], col[4])
                }

                // 0: Place, 1: Sail no, 2: Skipper, 3: Club, 4: Boat name, 5: Boat type
                DataFileType.M2S_CSV6 -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[4], col[5], col[2], col[3])
                }

                DataFileType.ULLERN_CSV -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[3] + "-" + col[4], col[6], col[5], col[1], col[2])
                }

                // 0: Place, 1: Sail no, 2: Boat type, 3: Boat name, 4: Skipper, 5: Club
                DataFileType.DROBAK_CSV -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[3], col[2], col[4], col[5])
                }

                else -> {
                    entry.placeNo = col[0].toInt()
                    entry.boat = Boat(col[1], col[2], col[5], col[3], col[4])
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            println("Error in line: $col")
        }
        entry.classSize = classSize
        entry.score = entry.placeNo * SeriesEntry.SCORE_FACTOR * 1.0 / classSize
        return entry
    }

    private fun findClassStart(entries: List<Array<String>>, startIndex: Int): Int {
        for (ix in startIndex until entries.size) {
            try {
                entries[ix][0].toInt()
                if (entries[ix].size <= 1) {
                    continue
                }
                return ix
            } catch (e: NumberFormatException) {
                // Do nothing
            }
        }
        return -1
    }

    private fun findClassSize(entries: List<Array<String>>, classStartIndex: Int): Int {
        for (ix in classStartIndex + 1 until entries.size) {
            try {
                entries[ix][0].toInt()
                if (entries[ix].size <= 1) {
                    return ix - classStartIndex
                }
            } catch (e: NumberFormatException) {
                return ix - classStartIndex
            }
        }
        return entries.size - classStartIndex
    }

    private fun getFileName(race: RaceData): String {
        return BigBoat.DATA + "/" + race.dataFileName + ".csv"
    }

    companion object {
        val instance = CsvReader()
    }
}
