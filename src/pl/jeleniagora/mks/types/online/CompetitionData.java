package pl.jeleniagora.mks.types.online;

import java.util.Vector;

/**
 * Klasa używana do aktualizacji czasów w konkurencji po każdym zakończonym ślizgu. Dla
 * ułatwienia tworzenia aplikacji za każdym razem przesyła się kompletne dane dla danej konkurencji, 
 * które są za każdym razem podmienione w bazie danych po stronie serwera
 * @author mateusz
 *
 */
public class CompetitionData {

	public int compId;
	
	/**
	 * Wektor imion i nazwisk zawodników
	 */
	public Vector<String> competitorsNames;
	
	/**
	 * Wektor nazw klubów zawodników
	 */
	public Vector<String> competitorsClubsNames;
	
	/**
	 * Wektor wektorów stringów określający czasy przejazdu wszystkich zawodników.
	 * Zewnętrzny wektor określa zawodników zgodnie z kolejnością w competitorsNames.
	 * Wewnętrzny wektor to kolejne czasy ślizgów przerobione do postaci stringa
	 */
	public Vector<Vector<String>> competitorsTimesStrings;
}
