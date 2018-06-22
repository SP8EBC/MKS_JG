package pl.jeleniagora.mks.files.xml.adapters;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

@Component
public class TotalTimesMapAdapter extends XmlAdapter<TotalTimesMapAdapter.AdaptedTimesMap, Map<LugerCompetitor, LocalTime>> {

	static RTE_ST rte_st;
	
	@Autowired
	void setRte(RTE_ST rte) {
		rte_st = rte;
	}
	
	public static class AdaptedTimesMap {
		public List<TimeMapEntry> runTimeInMicrotime;
	}
	
	public static class TimeMapEntry {
		
		@XmlElement(required = true)
		public Long lugerCompetitorSystemId;

		@XmlElement(required = true)
		public Integer microTime;
	}

	@Override
	public AdaptedTimesMap marshal(Map<LugerCompetitor, LocalTime> v) throws Exception {
		
		AdaptedTimesMap out = new AdaptedTimesMap();
		
		out.runTimeInMicrotime = new ArrayList<TimeMapEntry>();
		
		for (Entry<LugerCompetitor, LocalTime> e : v.entrySet()) {
			TimeMapEntry te = new TimeMapEntry();
			
			te.lugerCompetitorSystemId = e.getKey().getSystemId();
			te.microTime = ConvertMicrotime.fromLocalTime(e.getValue());
			
			out.runTimeInMicrotime.add(te);
		}
		
		return out;
	}

	@Override
	public Map<LugerCompetitor, LocalTime> unmarshal(AdaptedTimesMap v) throws Exception {
		
		if (rte_st == null)
			throw new RteIsNullEx();
		
		Map<LugerCompetitor, LocalTime> out = new HashMap<LugerCompetitor, LocalTime>();
		
		for (TimeMapEntry e : v.runTimeInMicrotime) {
			LugerCompetitor cmptr = rte_st.competitions.findLugerCompetitorBySystemId(e.lugerCompetitorSystemId);
			
			LocalTime t = ConvertMicrotime.toLocalTime(e.microTime);
			
			out.put(cmptr, t);
		}
		
		return out;
	}

}
