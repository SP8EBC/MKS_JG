package pl.jeleniagora.mks.scoring;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map.Entry;

import pl.jeleniagora.mks.types.LugerCompetitor;

public class EntryLugerCmptrLocalTimeComparator implements Comparator<Entry<LugerCompetitor, LocalTime>> {

	@Override
	public int compare(Entry<LugerCompetitor, LocalTime> o1, Entry<LugerCompetitor, LocalTime> o2) {

		LocalTime _o1 = o1.getValue();
		LocalTime _o2 = o2.getValue();
		
		if (_o1.equals(_o2))
			return 0;
		else if (_o1.isAfter(_o2))
			return 1;
		else
			return -1;
		
	}

}
