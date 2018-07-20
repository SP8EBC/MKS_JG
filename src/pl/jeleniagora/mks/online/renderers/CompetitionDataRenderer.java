package pl.jeleniagora.mks.online.renderers;

import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.Vector;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;
import pl.jeleniagora.mks.types.online.CompetitionData;

public class CompetitionDataRenderer {

	public static CompetitionData renderer(Competition competition) {
		
		// zmienna lokalna określająca pierwszy obiekt pętli, blokuje dodawanie do out.runsType
		boolean first = true;	
		
		CompetitionData out = new CompetitionData();
		
		out.compId = competition.id;
		
		for (Entry<Short, LugerCompetitor> e : competition.invertedStartList.entrySet()) {
			
			Short k = e.getKey();
			LugerCompetitor v = e.getValue();
			
			out.competitorsStartNums.add(new Integer(k));
			out.competitorsNames.addElement(v.toString());
			out.competitorsRanks.addElement(new Integer(k));
			
			out.competitorsRanks.add(new Integer(competition.ranks.get(v)));
			out.competitorsClubsNames.add(v.clubToString());
			
			Vector<String> times = new Vector<String>(); 	// czasy przejazdu - ślizgu dla tego saneczkarza
			
			for (Run r : competition.runsTimes) {
				if (first) {
					if (r.trainingOrScored)
						out.runsType.add(true);
					else 
						out.runsType.add(false);
					
					first = false;
				}
				
				LocalTime t = r.totalTimes.get(v);
				times.add(t.toString());
			}
			
			out.competitorsTimesStrings.add(times);
		}
		
		
		return out;
		
	}
}
