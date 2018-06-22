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
public class InvertedStartListAdapter extends XmlAdapter<InvertedStartListAdapter.AdaptedInvertedStartList, Map<Short, LugerCompetitor>> {

	static RTE_ST rte_st;

	@Autowired
	public void setRTE(RTE_ST rte) {
		rte_st = rte;
	}
	
	public static class AdaptedInvertedStartList {
		@XmlElement(name="ivertedStartListEntry")
		public List<AdaptedInvertedStartListEntry> adaptedList = new ArrayList<AdaptedInvertedStartListEntry>();
	}
	
	public static class AdaptedInvertedStartListEntry {
		
		@XmlElement(required = true)
		public short startNumber;
		
		@XmlElement(required = false, nillable = true )
		public Long lugerCompetitorSystemId;		
		
	}

	@Override
	public AdaptedInvertedStartList marshal(Map<Short, LugerCompetitor> arg0) throws Exception {
		AdaptedInvertedStartList out = new AdaptedInvertedStartList();
		
		for (Entry<Short, LugerCompetitor> e : arg0.entrySet()) {
			AdaptedInvertedStartListEntry adaptedEntry = new AdaptedInvertedStartListEntry();
			
			Short k = e.getKey();
			LugerCompetitor v = e.getValue();
			
			adaptedEntry.lugerCompetitorSystemId = v.getSystemId();
			adaptedEntry.startNumber = v.getStartNumber();
			
			out.adaptedList.add(adaptedEntry);
		}
		
		return out;
	}

	@Override
	public Map<Short, LugerCompetitor> unmarshal(AdaptedInvertedStartList arg0) throws Exception {
		Map<Short, LugerCompetitor> out = new HashMap<Short, LugerCompetitor>();
		
		for (AdaptedInvertedStartListEntry e : arg0.adaptedList) {
			LugerCompetitor cmptr = rte_st.competitions.findLugerCompetitorBySystemId(e.lugerCompetitorSystemId);

			
			out.put(e.startNumber, cmptr);
		}
		
		return out;
	}

}
