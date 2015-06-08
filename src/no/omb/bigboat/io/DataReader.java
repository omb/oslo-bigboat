package no.omb.bigboat.io;

import java.util.List;

import no.omb.bigboat.data.RaceData;
import no.omb.bigboat.data.RaceEntry;

public interface DataReader {

	List<RaceEntry> parseDataFile(RaceData race);

}
