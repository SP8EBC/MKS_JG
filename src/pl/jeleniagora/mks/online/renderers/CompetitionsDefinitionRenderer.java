package pl.jeleniagora.mks.online.renderers;

import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;

/**
 * Klasa generuje obiekt klasy CompetitionsDefinition
 * @author mateusz
 *
 */
public class CompetitionsDefinitionRenderer {

	public static CompetitionsDefinition render(Competitions competitions) {
		
		CompetitionsDefinition out = new CompetitionsDefinition();
		
		out.compCount = competitions.competitions.size();
		out.competitionsDate = competitions.date;
		out.competitionsName = competitions.name;
		out.trackName = competitions.track.toString();
		
		out.location = competitions.track.location;
		
		out.judge1 = competitions.Judge1st;
		out.judge2 = competitions.Judge2nd;
		out.judge3 = competitions.Judge3rd;
		
		out.organizer = competitions.organizer;
		
		return out;
		
	}
}
