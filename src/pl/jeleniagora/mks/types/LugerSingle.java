package pl.jeleniagora.mks.types;

/**
 * Ta klasa definiuje (enkapsuluje) saneczkarza-zawodnika (albo zawodniczke) w jedynkach
 * @author mateusz
 *
 */
public class LugerSingle implements LugerCompetitor {

	public Luger single;
	
	CompetitionTypes type;
		
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

}
