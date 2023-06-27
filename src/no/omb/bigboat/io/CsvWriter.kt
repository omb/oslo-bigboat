package no.omb.bigboat.io

import au.com.bytecode.opencsv.CSVWriter
import no.omb.bigboat.BigBoat
import no.omb.bigboat.data.RaceData
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class CsvWriter : AbstractWriter() {

    fun writeRaceResultFile(race: RaceData) {
        val fileName = BigBoat.RESULTS + "/" + race.dataFileName + ".csv"
        val writer: CSVWriter
        try {
            val os = FileOutputStream(fileName)
            writer = CSVWriter(OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER)
            writer.writeNext(raceResultHeader.split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            var place = 1
            for (entry in BigBoat.getRaceResults()[race]!!) {
                val line: Array<String> = entry.toString().split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                writer.writeNext(addPlace(line, place))
                place++
            }
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeSeriesResultFile() {
        val fileName = BigBoat.RESULTS + "/" + "sammenlagt" + ".csv"
        val list = BigBoat.sortedSeries
        val writer: CSVWriter
        try {
            val os = FileOutputStream(fileName)
            writer = CSVWriter(OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER)
            writer.writeNext(seriesResultHeader.split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            var place = 1
            for (entry in list) {
                val line: Array<String> = entry.toString().split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                writer.writeNext(addPlace(line, place))
                place++
            }
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeClubsResultFile() {
        val fileName = BigBoat.RESULTS + "/" + "beste-seilforening" + ".csv"
        val list = BigBoat.sortedClubs
        val writer: CSVWriter
        try {
            val os = FileOutputStream(fileName)
            writer = CSVWriter(OutputStreamWriter(os, BigBoat.CHARSET), ';', CSVWriter.NO_QUOTE_CHARACTER)
            writer.writeNext(clubsResultHeader.split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            var place = 1
            for (entry in list) {
                val line: Array<String> = entry.toString().split(BigBoat.SEP.toString().toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                writer.writeNext(addPlace(line, place))
                place++
            }
            writer.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        val instance = CsvWriter()
    }
}
