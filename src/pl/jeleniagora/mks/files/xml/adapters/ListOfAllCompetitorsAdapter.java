package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

public class ListOfAllCompetitorsAdapter extends XmlAdapter<ListOfAllCompetitorsAdapter.AdaptedCompetitorsList, List<LugerCompetitor>> {

	public static class AdaptedCompetitorsList {
		@XmlElement(name = "competitor")
		public List<AdaptedCompetitorsListEntry> competitorsList;
	}
	
	public static class AdaptedCompetitorsListEntry {
		@XmlElement(required = false, nillable = true )
		public Long singleLugerSystemId;			// konkurencja pojedyncza K albo M

		@XmlElement(required = false, nillable = true )
		public Long lowerLugerSystemId;	// dwójki sankowe - sankarz na dole
		
		@XmlElement(required = false, nillable = true )
		public Long upperLugerSystemId;		// j/w ale sankarz na górze
		
		@XmlElement(required = false, nillable = true )
		public Long maleLugerSystemId;		// M podczas sztafety albo konkurencji drużynowej
		
		@XmlElement(required = false, nillable = true )
		public Long femaleLugerSystemId;	// K j/w
		
		@XmlElement(required = true)
		public Long lugerCompetitorSystemId;
	}

	@Override
	public AdaptedCompetitorsList marshal(List<LugerCompetitor> v) throws Exception {
		AdaptedCompetitorsList out = new AdaptedCompetitorsList();
		
		out.competitorsList = new ArrayList<AdaptedCompetitorsListEntry>();
		
		for (LugerCompetitor e : v) {
			
			if (e instanceof LugerSingle) {
				AdaptedCompetitorsListEntry ae = new AdaptedCompetitorsListEntry();
				
				ae.singleLugerSystemId = ((LugerSingle)e).single.getSystemId();
				ae.lugerCompetitorSystemId = e.getSystemId();
				
				out.competitorsList.add(ae);
			}
			else if (e instanceof LugerDouble) {
				AdaptedCompetitorsListEntry ae = new AdaptedCompetitorsListEntry();
				
				ae.lowerLugerSystemId = ((LugerDouble)e).lower.getSystemId();
				ae.upperLugerSystemId = ((LugerDouble)e).upper.getSystemId();
				ae.lugerCompetitorSystemId = e.getSystemId();
				
				out.competitorsList.add(ae);
			}
			else;
		}
		
		return out;
	}

	@Override
	public List<LugerCompetitor> unmarshal(AdaptedCompetitorsList v) throws Exception {
		return null;
	}

}
