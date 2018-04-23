package pl.jeleniagora.mks.factories;

import pl.jeleniagora.mks.types.Club;

public class ClubsFactory {

	public static Club createNewFromName(String name) {
		Club c = new Club();
		
		c.name = name;
		
		return c;
	}
}
