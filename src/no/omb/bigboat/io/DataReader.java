package no.omb.bigboat.io;

import java.util.List;

import no.omb.bigboat.RaceData;
import no.omb.bigboat.RaceEntry;

public interface DataReader {

	List<RaceEntry> parseDataFile(RaceData race);

}
