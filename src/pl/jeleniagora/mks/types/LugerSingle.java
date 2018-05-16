package pl.jeleniagora.mks.types;

/**
 * Ta klasa definiuje (enkapsuluje) saneczkarza-zawodnika (albo zawodniczke) w jedynkach
 * @author mateusz
 *
 */
public class LugerSingle implements LugerCompetitor {

	public Luger single;
	
	CompetitionTypes type;
	
	short startNum;
		
	public LugerSingle(boolean isFemale) {
		if (isFemale)
			type = CompetitionTypes.WOMAN_SINGLE;
		else
			type = CompetitionTypes.MEN_SINGLE;
	}
	
	@Override
	public CompetitionTypes getCompetitorType() {
		// TODO Auto-generated method stub
		return type;
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

}
