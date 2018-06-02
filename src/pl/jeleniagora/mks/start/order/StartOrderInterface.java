package pl.jeleniagora.mks.start.order;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;

public interface StartOrderInterface {

	public Short nextStartNumber(Short currentStartNumber, Competition currentCompetition);
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition);

}
