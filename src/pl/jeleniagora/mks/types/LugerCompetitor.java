package pl.jeleniagora.mks.types;

/**
 * Interfejs używany do enkapsulacji saneczkarzy w różnych typach konkurencji sankowych, zarówno jedynek jak i dwójek oraz
 * drużyn. 
 * @author mateusz
 *
 */
public interface LugerCompetitor {

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
	public CompetitionTypes getCompetitorType();
	
	public void setStartNumber(short num);
	public short getStartNumber();
	
	public String toString();
}
