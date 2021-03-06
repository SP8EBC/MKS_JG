package pl.jeleniagora.mks.rte;

import java.util.Vector;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
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
	 * Referencja na obiekt "Competitions" który jest głównym i nadrzędnym elementem struktury danych w programie
	 */
	public Competitions competitions;
	
	/**
	 * Zawody aktualnie wybrane w menadżerze zawodów i wyświetlane w jego głównej tabeli.
	 */
	public Competition currentCompetition;
	
	public Run currentRun;
	
	/**
	 * Pole przechowuje numer kolejny ślizgu w danej konkurencji. Ślizgi są liczone normalnie po programistycznemu,
	 * czyli od zera a nie od jedynki.
	 */
	public short currentRunCnt;

	/**
	 * Wektor przechowywujący referencję do już rozegranych konkurencji
	 */
	public Vector<Competition> competitionsDone;
	
	/**
	 * Referencja na saneczkarza z którego nastąpiło przeskoczenie poza kolejke
	 */
	public LugerCompetitor returnComptr;
	
	/**
	 * Ustawienie na true powoduje że sankarz wskazany jako returnComptr został zamieniony
	 * z pozycji "aktualnie na torze" a nie następnie
	 */
	public boolean returnToActual;
	
	/**
	 * Referencja do "zawodnika aktualnie na torze"
	 */
	public LugerCompetitor actuallyOnTrack;
	/**
	 * Referencja do zawodnika "następnie"
	 */
	public LugerCompetitor nextOnTrack;
	
	/**
	 * Ustawienie zmiennej na true powoduje że program będzie próbował aktualizować
	 * wyniki online po każdym zakończonym ślizgu
	 */
	public boolean enableOnlineScoring = false;
	
	/**
	 * Nazwa otwartego bądź zapisanego pliku
	 */
	public String filename = null;
	
	/**
	 * Ścieżka katalogu w którym znajduje się otwaty bądź zapisany plik
	 */
	public String filePath = null;
	
	
}
