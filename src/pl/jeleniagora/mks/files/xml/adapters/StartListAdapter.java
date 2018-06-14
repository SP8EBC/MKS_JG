package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

import pl.jeleniagora.mks.rte.RTE_ST;

/**
 * Adapter używany do konwersji listy startowej na obiekt który może być łatwo i jednoznacznie zapisany w pliku XML
 * @author mateusz
 *
 */
@Component
public class StartListAdapter extends XmlAdapter<StartListAdapter.AdaptedStartList, Map<LugerCompetitor, Short>> {

	RTE_ST rte_st;

	@Autowired
	@Lazy
	public void setRTE(RTE_ST rte) {
		rte_st = rte;
	}
	
	public static class AdaptedStartList {
		@XmlElement(name="startListEntry")
		public List<AdaptedEntry> adaptedList = new ArrayList<AdaptedEntry>();
	}
	
	public static class AdaptedEntry {
		
		@XmlElement(required = false, nillable = true )
		public Long lugerSystemId;			// konkurencja pojedyncza K albo M

		@XmlElement(required = false, nillable = true )
		public Long lowerLugerSystemId;	// dwójki sankowe - sankarz na dole
		
		@XmlElement(required = false, nillable = true )
		public Long upperLugerSystemId;		// j/w ale sankarz na górze
		
		@XmlElement(required = false, nillable = true )
		public Long maleLugerSystemId;		// M podczas sztafety albo konkurencji drużynowej
		
		@XmlElement(required = false, nillable = true )
		public Long femaleLugerSystemId;	// K j/w
		
		@XmlElement(required = true)
		public short startNumber;
	}

	@Override
	public AdaptedStartList marshal(Map<LugerCompetitor, Short> arg0) throws Exception {
		AdaptedStartList out = new AdaptedStartList();
		
		for (Entry<LugerCompetitor, Short> e : arg0.entrySet()) {
			AdaptedEntry adaptedEntry = new AdaptedEntry();
			
			LugerCompetitor k = e.getKey();
			
			if (k instanceof LugerSingle) {
				adaptedEntry.lugerSystemId = ((LugerSingle)k).single.getSystemId();
				adaptedEntry.startNumber = e.getValue();
			}
			else if (k instanceof LugerDouble) {
				adaptedEntry.lowerLugerSystemId = ((LugerDouble)k).lower.getSystemId();
				adaptedEntry.upperLugerSystemId = ((LugerDouble)k).upper.getSystemId();
				adaptedEntry.startNumber = e.getValue();

			}
			
			out.adaptedList.add(adaptedEntry);
		}
		
		return out;
	}

	@Override
	public Map<LugerCompetitor, Short> unmarshal(AdaptedStartList arg0) throws Exception {
		return null;
	}

}
