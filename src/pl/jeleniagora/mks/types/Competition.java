package pl.jeleniagora.mks.types;

/**
 * Klasa przechowywująca wszystkie informację o pojedycznej konkurencji. Jej rodzaj (jedynki M, jedynki K, dwójki, sztafeta), jak
 * również referencje do listy startowej, liczbę ślizgów oraz mapy spinające każdego zawodnika 
 * 
 * @author mateusz
 *
 */

public class Competition {
	/* Egzemplarz enuma określający typ tej konkurencji */
	private CompetitionTypes competitionType;

	/* Liczba wszystkich (łącznie z treningowymi) ślizgów w tej konkurencji */ 
	private int numberOfAllRuns;
	
	/* Ilość ślizgów treningowych w konkurencji */
	private int numberOfTrainingRuns;
	
	public CompetitionTypes getCompetitionType() {
		return competitionType;
	}
	
	public Competition() {
		this.competitionType = CompetitionTypes.UNINITIALIZED_COMP;
		this.numberOfAllRuns = 0;
		this.numberOfTrainingRuns = 0;
	}

	public void setCompetitionType(CompetitionTypes competitionType) {
		this.competitionType = competitionType;
	}

	public int getNumberOfAllRuns() {
		return numberOfAllRuns;
	}

	public void setNumberOfAllRuns(int numberOfAllRuns) {
		this.numberOfAllRuns = numberOfAllRuns;
	}

	public int getNumberOfTrainingRuns() {
		return numberOfTrainingRuns;
	}

	public void setNumberOfTrainingRuns(int numberOfTrainingRuns) {
		this.numberOfTrainingRuns = numberOfTrainingRuns;
	}
	
	/* Tu ma być reszta rzeczy typu lista startowa itp.	 */
}
