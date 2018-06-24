package pl.jeleniagora.mks.types;

import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Klasa abstrakycjna używana do enkapsulacji saneczkarzy w różnych typach konkurencji sankowych, zarówno jedynek jak i dwójek oraz
 * drużyn. Wcześniej był tu interfejs ale musiało to być zmienione gdyż JAXB przy marschallingu i marshallingu nie może używać interfejsów
 * @author mateusz
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)

public abstract class LugerCompetitor {
	protected CompetitionTypes competitorType; // 24 czerwca zmienione z private -> protected
	
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
	public CompetitionTypes getCompetitorType() {
		return competitorType;
	}
	
	public void setCompetitorType(CompetitionTypes e) {
		this.competitorType = e;
	}
		
	public abstract void setStartNumber(short num);
	@XmlTransient
	public abstract short getStartNumber();
	
	public abstract String toString();
	
	protected Long competitorSystemId;	// 24 czerwca zmienione z private -> protected

	public void generateSystemId() {
		 this.competitorSystemId = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);

	}
	
	public Long getSystemId() {
		return this.competitorSystemId;
	}
	
	public void setSystemId(Long in) {
		this.competitorSystemId = in;
	}
	
}
