package pl.jeleniagora.mks.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ta klasa definiuje (enkapsuluje) saneczkarza-zawodnika (albo zawodniczke) w jedynkach
 * @author mateusz
 *
 */
public class LugerSingle extends LugerCompetitor {

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
