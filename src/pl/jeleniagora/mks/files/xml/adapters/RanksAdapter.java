package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

@Component
public class RanksAdapter extends XmlAdapter<RanksAdapter.AdaptedRanks, Map<LugerCompetitor, Short>> {

	static RTE_ST rte_st;

	@Autowired
	public void setRTE(RTE_ST rte) {
		rte_st = rte;
	}
	
	public static class AdaptedRanks {
		public List<AdapterRanksEntry> adaptedRank = new ArrayList<AdapterRanksEntry>();

	}
	
	public static class AdapterRanksEntry {
		@XmlElement(required = false, nillable = true )
		public Long lugerCompetitorSystemId;		
		
		@XmlElement(required = true)
		public short rank;
	}

	@Override
	public AdaptedRanks marshal(Map<LugerCompetitor, Short> arg0) throws Exception {
		AdaptedRanks out = new AdaptedRanks();
		
		for (Entry<LugerCompetitor, Short> e : arg0.entrySet()) {
			AdapterRanksEntry adaptedEntry = new AdapterRanksEntry();
			
			LugerCompetitor k = e.getKey();
			short v = e.getValue();
			
			adaptedEntry.lugerCompetitorSystemId = k.getSystemId();
			adaptedEntry.rank = v;
			
			out.adaptedRank.add(adaptedEntry);
		}
		
		return out;
	}

	@Override
	public Map<LugerCompetitor, Short> unmarshal(AdaptedRanks arg0) throws Exception {
		Map<LugerCompetitor, Short> out = new HashMap<LugerCompetitor, Short>();
		
		for (AdapterRanksEntry e : arg0.adaptedRank) {
			LugerCompetitor cmptr = rte_st.competitions.findLugerCompetitorBySystemId(e.lugerCompetitorSystemId);
			
			out.put(cmptr, e.rank);
		}
		
		return out;
	}

}
