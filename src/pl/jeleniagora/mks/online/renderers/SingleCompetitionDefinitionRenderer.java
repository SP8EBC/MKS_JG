package pl.jeleniagora.mks.online.renderers;

import java.util.Map.Entry;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.online.SingleCompetitionDefinition;

public class SingleCompetitionDefinitionRenderer {

	public SingleCompetitionDefinition render (Competition competition) {
		SingleCompetitionDefinition out = new SingleCompetitionDefinition();
		
		out.runsCount = competition.numberOfAllRuns;
		out.trainingRunsCount = competition.numberOfTrainingRuns;
		
		for (Entry<Short, LugerCompetitor> e : competition.invertedStartList.entrySet()) {
			
			Short k = e.getKey();
			LugerCompetitor v = e.getValue();
			
			out.competitorsStartNums.add(new Integer(k));
			
			out.competitorsNames.addElement(v.toString());
			out.competitorsClubsNames.add(v.clubToString());
		}		
		return out;
	}
}
