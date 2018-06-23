package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import pl.jeleniagora.mks.types.LugerCompetitor;

import pl.jeleniagora.mks.rte.RTE_ST;

/**
 * Adapter używany do konwersji listy startowej na obiekt który może być łatwo i jednoznacznie zapisany w pliku XML
 * @author mateusz
 *
 */
@Component
public class StartListAdapter extends XmlAdapter<StartListAdapter.AdaptedStartList, Map<LugerCompetitor, Short>> {

	static RTE_ST rte_st;

	@Autowired
	public void setRTE(RTE_ST rte) {
		rte_st = rte;
	}
	
	public static class AdaptedStartList {
		@XmlElement(name="startListEntry")
		public List<AdaptedStartListEntry> adaptedList = new ArrayList<AdaptedStartListEntry>();
	}
	
	public static class AdaptedStartListEntry {
		
		@XmlElement(required = false, nillable = true )
		public Long lugerCompetitorSystemId;		
		
		@XmlElement(required = true)
		public short startNumber;
	}

	@Override
	public AdaptedStartList marshal(Map<LugerCompetitor, Short> arg0) throws Exception {
		AdaptedStartList out = new AdaptedStartList();
		
		for (Entry<LugerCompetitor, Short> e : arg0.entrySet()) {
			AdaptedStartListEntry adaptedEntry = new AdaptedStartListEntry();
			
			LugerCompetitor k = e.getKey();
			
			adaptedEntry.lugerCompetitorSystemId = k.getSystemId();
			adaptedEntry.startNumber = k.getStartNumber();
			
			out.adaptedList.add(adaptedEntry);
		}
		
		return out;
	}

	@Override
	public Map<LugerCompetitor, Short> unmarshal(AdaptedStartList arg0) throws Exception {
		Map<LugerCompetitor, Short> out = new HashMap<LugerCompetitor, Short>();
		
		for (AdaptedStartListEntry e : arg0.adaptedList) {
			LugerCompetitor cmptr = rte_st.competitions.findLugerCompetitorBySystemId(e.lugerCompetitorSystemId);
			cmptr.setStartNumber(e.startNumber);
			
			out.put(cmptr, e.startNumber);
		}
		
		return out;
	}

}
