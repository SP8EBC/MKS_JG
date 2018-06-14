package pl.jeleniagora.mks.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Klasa abstrakycjna używana do enkapsulacji saneczkarzy w różnych typach konkurencji sankowych, zarówno jedynek jak i dwójek oraz
 * drużyn. Wcześniej był tu interfejs ale musiało to być zmienione gdyż JAXB przy marschallingu i marshallingu nie może używać interfejsów
 * @author mateusz
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)

public abstract class LugerCompetitor {
	/**
	 * Metoda zwracająca typ tego saneczkarza-zawodnika, co jest toższame z konkurencją w jakiej będzie on
	 * startował. Jeżei dostanie się nazad MEN_SINGLE albo WOMAN_SINGLE będzie to oznaczało, że klasa implementująca
	 * ten intefejs enkapsuluje jednego saneczkarza. DOUBLE_cokolwiek oznacza że enkapsuluje dwóch (góra i dół).
	 * Analogicznie TEAM_RELAY itp.
	 * 
	 * Działanie tegoż jest podobne do wywołania:
	 * 		if (LugerCompetitor iksinski instanceof LugerSingle) {;}
	 * 
	 * @return
	 */	
	public abstract CompetitionTypes getCompetitorType();
		
	public abstract void setStartNumber(short num);
	@XmlTransient
	public abstract short getStartNumber();
	
	public abstract String toString();
	
}
