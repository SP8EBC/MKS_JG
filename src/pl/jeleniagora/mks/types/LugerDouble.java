package pl.jeleniagora.mks.types;

/**
 * Klasa enkapsulująca dwójke sankową.
 * @author mateusz
 *
 */
public class LugerDouble extends LugerCompetitor {

	/**
	 * Saneczkarz na górze
	 */
	public Luger upper;
	
	/**
	 * Saneczkarz na dole
	 */
	public Luger lower;
	
	short startNum;
	
	public LugerDouble() {
		this.generateSystemId();	//  z klasy bazowej
	}
	
	public String toString() {
		String out;
		
		out = upper.surname + " / " + lower.surname;
		
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
