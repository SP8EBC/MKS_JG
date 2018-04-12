package pl.jeleniagora.mks.types;

import java.util.Map;

/**
 * Klasa przechowywująca wszystkie informację o pojedycznej konkurencji. Jej rodzaj (jedynki M, jedynki K, dwójki, sztafeta), jak
 * również referencje do listy startowej, liczbę ślizgów oraz mapy spinające każdego zawodnika 
 * 
 * @author mateusz
 *
 */

public class Competition {
	/**
	 *  Egzemplarz enuma określający typ tej konkurencji 
	 *  */
	public CompetitionTypes competitionType;

	/**
	 *  Liczba wszystkich (łącznie z treningowymi) ślizgów w tej konkurencji.
	 *   */ 
	public int numberOfAllRuns;
	
	/** 
	 * Ilość ślizgów treningowych w konkurencji 
	 * */
	public int numberOfTrainingRuns;
	
	/**
	 * Mapa łącząca saneczkarzy z numerami startowymi.
	 */
	Map<Luger, Short> startList;
	
	/**
	 * Mapa łącząca saneczkarzy z miejscami (lokatami) które uzyskali.
	 */
	Map<Luger, Short> scores;
	
	public Competition() {
		this.competitionType = CompetitionTypes.UNINITIALIZED_COMP;
		this.numberOfAllRuns = 0;
		this.numberOfTrainingRuns = 0;
	}
	
	/* Tu ma być reszta rzeczy typu lista startowa itp.	 */
}
