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

	public CompetitionData() {
		competitorsNames = new Vector<String>();
		competitorsRanks = new Vector<Integer>();
		competitorsClubsNames = new Vector<String>();
		
		competitorsTimesStrings = new Vector<Vector<String>>();
		competitorsStartNums = new Vector<Integer>();
	}
	
	public int compId;
	
	/**
	 * Wektor imion i nazwisk zawodników
	 */
	public Vector<String> competitorsNames;
	
	/**
	 * Numery startowe
	 */
	public Vector<Integer> competitorsStartNums;
	
	/**
	 * Lokaty zawodników
	 */
	public Vector<Integer> competitorsRanks;
	
	/**
	 * Wektor nazw klubów zawodników
	 */
	public Vector<String> competitorsClubsNames;
	
	/**
	 * Rodzaj kolejnych ślizgów zgodnie z kolejnością w wewnętrznym wektorze 'competitorsTimesStrings', false
	 * oznacza ślizg treningowy, true punktowany
	 */
	public Vector<Boolean> runsType;
	
	/**
	 * Wektor wektorów stringów określający czasy przejazdu wszystkich zawodników.
	 * Zewnętrzny wektor określa zawodników zgodnie z kolejnością w competitorsNames.
	 * Wewnętrzny wektor to kolejne czasy ślizgów przerobione do postaci stringa
	 */
	public Vector<Vector<String>> competitorsTimesStrings;
}
