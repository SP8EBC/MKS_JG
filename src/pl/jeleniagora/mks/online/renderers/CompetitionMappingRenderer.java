package pl.jeleniagora.mks.online.renderers;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionsToCompetitionMapping;
import pl.jeleniagora.mks.types.online.CompetitionsToCompetitionMappingEntry;

public class CompetitionMappingRenderer {

	public static CompetitionsToCompetitionMapping render(Competitions competitions) {
		CompetitionsToCompetitionMapping out = new CompetitionsToCompetitionMapping();
		
		String name = competitions.name;
		
		for (Competition elem : competitions.competitions) {
			CompetitionsToCompetitionMappingEntry e = new CompetitionsToCompetitionMappingEntry();
			
			e.competitionsName = name;
			e.competitionSerialNumber = elem.serialNum;
			
			out.entries.add(e);
		}
		
		return out;
	}
}
