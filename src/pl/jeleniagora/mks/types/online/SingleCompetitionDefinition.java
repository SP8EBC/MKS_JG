package pl.jeleniagora.mks.types.online;

import java.util.Vector;

/**
 * Definicja pojedynczej konkurencji w zawodach
 * @author mateusz
 *
 */
public class SingleCompetitionDefinition {
	
	public SingleCompetitionDefinition() {
		competitorsStartNums = new Vector<Integer>();
		competitorsNames = new Vector<String>();
		competitorsClubsNames = new Vector<String>();
	}

	/**
	 * Identyfikator numeryczny konkurencji
	 */
	public int compId;
	
	/**
	 * Nazwa konkurencji
	 */
	public String name;
	
	/**
	 * Liczba zawodników w konkurencji
	 */
	public int competitorsNumber;
	
	/**
	 * Numery startowe
	 */
	public Vector<Integer> competitorsStartNums;
	
	/**
	 * Wektor imion i nazwisk zawodników
	 */
	public Vector<String> competitorsNames;
	
	/**
	 * Wektor nazw klubów zawodników
	 */
	public Vector<String> competitorsClubsNames;
	
	/**
	 * Liczba wszystkich ślizgów;
	 */
	public int runsCount;
	
	/**
	 * Liczba ślizgów treningowych
	 */
	public int trainingRunsCount;
}
