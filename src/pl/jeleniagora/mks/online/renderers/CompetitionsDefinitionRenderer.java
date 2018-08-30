package pl.jeleniagora.mks.online.renderers;

import javax.swing.JOptionPane;

import pl.jeleniagora.mks.exceptions.CompetitionDefinitionRenderEx;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;

/**
 * Klasa generuje obiekt klasy CompetitionsDefinition
 * @author mateusz
 *
 */
public class CompetitionsDefinitionRenderer {

	public static CompetitionsDefinition render(Competitions competitions) throws CompetitionDefinitionRenderEx {
		
		CompetitionsDefinition out = new CompetitionsDefinition();
		
		if (competitions != null && competitions.competitions != null)
			out.compCount = competitions.competitions.size();
		else {
			JOptionPane.showMessageDialog(null, "Nie można dodać zawodów do systemu z powodu ich złej inicjalizacji. Załaduj plik XML jeszcze raz..");
			throw new CompetitionDefinitionRenderEx();

		}
		
		if (competitions.name == null || competitions.name.equals("")) {
			JOptionPane.showMessageDialog(null, "Nie wprowadziłeś nazwy zawodów/treningu, która jest wymagana aby dodać je do systemu internetowego");
			throw new CompetitionDefinitionRenderEx();			
		}

		if (competitions.date == null) {
			JOptionPane.showMessageDialog(null, "Nie wprowadziłeś daty rozgrywania zawodów, która jest wymagana aby dodać je do systemu internetowego");
			throw new CompetitionDefinitionRenderEx();			
		}
		
		out.competitionsDate = competitions.date;
		out.competitionsName = competitions.name;
		if (competitions.track != null)
			out.trackName = competitions.track.toString();
		else {
			JOptionPane.showMessageDialog(null, "Aby dodać zawody do systemu internetowego musisz wybrać tor na którym są rozgrywane");
			throw new CompetitionDefinitionRenderEx();
			//return;
		}
		
		out.location = competitions.track.location;
		
		out.judge1 = competitions.Judge1st;
		out.judge2 = competitions.Judge2nd;
		out.judge3 = competitions.Judge3rd;
		
		out.organizer = competitions.organizer;
		
		return out;
		
	}
}
