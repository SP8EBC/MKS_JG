package pl.jeleniagora.mks.types;

/**
 * Typ używany do enkapsulacji Konkurencji (Competition) na potrzeby listy wybieranej w CompManagerze. 
 */
public class CompetitionEncapsulationForSelector {
	private Competition competition;
	
	public CompetitionEncapsulationForSelector(Competition in) {
		setCompetition(in);
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}
	
	@Override
	public String toString() {
		return this.competition.toString();
	}
}
