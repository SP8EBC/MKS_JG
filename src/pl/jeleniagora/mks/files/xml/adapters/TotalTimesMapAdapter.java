package pl.jeleniagora.mks.files.xml.adapters;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.LugerCompetitor;

public class TotalTimesMapAdapter extends XmlAdapter<TotalTimesMapAdapter.AdaptedTimesMap, Map<LugerCompetitor, LocalTime>> {

	public static class AdaptedTimesMap {
		public List<TimeMapEntry> runTimeInMicrotime;
	}
	
	public static class TimeMapEntry {
		
		@XmlElement(required = true)
		public short startNumber;
		
		public short time;
	}

	@Override
	public AdaptedTimesMap marshal(Map<LugerCompetitor, LocalTime> v) throws Exception {
		
		AdaptedTimesMap out = new AdaptedTimesMap();
		
		for (Entry<LugerCompetitor, LocalTime> e : v.entrySet()) {
			
		}
		
		return null;
	}

	@Override
	public Map<LugerCompetitor, LocalTime> unmarshal(AdaptedTimesMap v) throws Exception {
		return null;
	}

}
