package pl.jeleniagora.mks.rte;

import java.util.Vector;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * 
 * @author mateusz
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_ST {

	public RTE_ST() {
		competitionsDone = new Vector<Competition>();
	}
	
	/**
	 * Zawody aktualnie wybrane w menadżerze zawodów i wyświetlane w jego głównej tabeli.
	 */
	public Competition currentCompetition;
	
	public Run currentRun;

	/**
	 * Wektor przechowywujący referencję do już rozegranych konkurencji
	 */
	public Vector<Competition> competitionsDone;
	
	/**
	 * Referencja do "zawodnika aktualnie na torze"
	 */
	public LugerCompetitor actuallyOnTrack;
	/**
	 * Referencja do zawodnika "następnie"
	 */
	public LugerCompetitor nextOnTrack;
	
	/**
	 * Referencja do pozycji wskazanej w CompManager
	 */
	public LugerCompetitor selectedInCompManager;
}