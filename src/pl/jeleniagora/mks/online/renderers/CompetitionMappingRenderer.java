package pl.jeleniagora.mks.online.renderers;

import java.util.Vector;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionsToCompetitionMapping;
import pl.jeleniagora.mks.types.online.CompetitionsToCompetitionMappingEntry;

public class CompetitionMappingRenderer {

	public static CompetitionsToCompetitionMapping render(Competitions competitions) {
		CompetitionsToCompetitionMapping out = new CompetitionsToCompetitionMapping();
		out.entries = new Vector<CompetitionsToCompetitionMappingEntry>();
		
		String name = competitions.name;
		
		for (Competition elem : competitions.competitions) {
			CompetitionsToCompetitionMappingEntry e = new CompetitionsToCompetitionMappingEntry();
			
			e.competitionsName = name;
			e.competitionSerialNumber = elem.serialNum;
			e.competitionTypeName = elem.toString();
			
			out.entries.add(e);
		}
		
		return out;
	}
}
