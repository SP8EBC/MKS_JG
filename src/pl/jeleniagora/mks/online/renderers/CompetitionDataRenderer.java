package pl.jeleniagora.mks.online.renderers;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map.Entry;

import org.springframework.format.annotation.DateTimeFormat;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerSingle;
import pl.jeleniagora.mks.types.Run;
import pl.jeleniagora.mks.types.online.CompetitionData;
import pl.jeleniagora.mks.types.online.CompetitionDataEntry;

public class CompetitionDataRenderer {

	public static CompetitionData renderer(Competition competition) {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("m:ss.SSS");
		
		CompetitionData out = new CompetitionData();
		
		out.competitionSerialNumber = competition.serialNum;
		out.competitionId = competition.id;
		
		for (Entry<Short, LugerCompetitor> e : competition.invertedStartList.entrySet()) {
			LocalTime totalScoredTime = LocalTime.of(0, 0, 0, 0);
			
			Short k = e.getKey();
			LugerCompetitor v = e.getValue();
			
			CompetitionDataEntry entry = new CompetitionDataEntry();
			
			entry.clubName = v.clubToString();
			entry.competitorName = v.toString();
			entry.competitorStartNumber = new Integer(k);
			entry.competitorRank = competition.ranks.get(v);
			if (competition.partialRanks != null && competition.partialRanks.containsKey(v))
				entry.competitorPartialRank = competition.partialRanks.get(v);
			else
				entry.competitorPartialRank = 0;

			
			for (Run r : competition.runsTimes) {
				if (r.trainingOrScored) {
					entry.scoredRunsTimesStr.add(r.totalTimes.get(v).format(fmt));
					Duration toAdd = Duration.between(LocalTime.MIDNIGHT, r.totalTimes.get(v));
					totalScoredTime = totalScoredTime.plus(toAdd);
				}
				else {

					entry.trainingRunsTimesStr.add(r.totalTimes.get(v).format(fmt));					
				}
				
				
			}
			entry.totalScoredTimeStr = totalScoredTime.format(fmt);
			
			// wyciaganie typu tego zawodnika (jedynki, dwójki itp)
			CompetitionTypes comptrType = v.getCompetitorType();
			
			if (comptrType.equals(CompetitionTypes.MEN_SINGLE) ||
					comptrType.equals(CompetitionTypes.WOMAN_SINGLE) ) 
			{
				// konkurencja jedynek
				entry.birthYear = ((LugerSingle)v).single.birthDate.getYear();
			}
			
			out.entry.add(entry);
			//out.competitorsTimesStrings.add(times);
		}
		
		
		return out;
		
	}
}
