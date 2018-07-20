package pl.jeleniagora.mks.types;

/**
 * Ta klasa definiuje (enkapsuluje) saneczkarza-zawodnika (albo zawodniczke) w jedynkach
 * @author mateusz
 *
 */
public class LugerSingle extends LugerCompetitor {

	public Luger single;
		
	short startNum;
		
	public LugerSingle(boolean isFemale) {
		this.generateSystemId(); // z klasy bazowej
		
		if (isFemale)
			this.competitorType = CompetitionTypes.WOMAN_SINGLE;
		else
			this.competitorType = CompetitionTypes.MEN_SINGLE;
	}
	
	public String toString() {
		String out = single.name + " " + single.surname;
		return out;
	}

	@Override
	public short getStartNumber() {
		return this.startNum;
	}

	@Override
	public void setStartNumber(short num) {
		this.startNum = num;
	}

	@Override
	public String clubToString() {
		// TODO Auto-generated method stub
		return single.club.name;
	}

}
