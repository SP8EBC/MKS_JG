package pl.jeleniagora.mks.types.online;

import java.util.Vector;

public class CompetitionDataEntry {

	public CompetitionDataEntry() {
		trainingRunsTimesStr = new Vector<String>();
		scoredRunsTimesStr = new Vector<String>();
	}
	
	public String competitorName;
	
	public int competitorStartNumber;
	
	public int competitorRank;
	
	public int competitorPartialRank;
	
	public String clubName;
	
	public Vector<String> trainingRunsTimesStr;
	
	public Vector<String> scoredRunsTimesStr;
}
