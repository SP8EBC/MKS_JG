package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.CompetitionsNotCreatedEx;
import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.CompetitionTypes;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

@Component
public class ListOfAllCompetitorsAdapter extends XmlAdapter<ListOfAllCompetitorsAdapter.AdaptedCompetitorsList, List<LugerCompetitor>> {

	static RTE_ST rte_st;
	
	@Autowired
	@Lazy
	public void setRte(RTE_ST rte) {
		rte_st = rte;
	}
	
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
		CompetitionTypes lugerCompetitorType;
		
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
				ae.lugerCompetitorType = e.getCompetitorType();
				
				out.competitorsList.add(ae);
			}
			else if (e instanceof LugerDouble) {
				AdaptedCompetitorsListEntry ae = new AdaptedCompetitorsListEntry();
				
				ae.lowerLugerSystemId = ((LugerDouble)e).lower.getSystemId();
				ae.upperLugerSystemId = ((LugerDouble)e).upper.getSystemId();
				ae.lugerCompetitorSystemId = e.getSystemId();
				ae.lugerCompetitorType = e.getCompetitorType();
				
				out.competitorsList.add(ae);
			}
			else;
		}
		
		return out;
	}

	@Override
	public List<LugerCompetitor> unmarshal(AdaptedCompetitorsList v) throws Exception {
		List<LugerCompetitor> out = new ArrayList<LugerCompetitor>();
		
		if (rte_st == null)
			throw (new RteIsNullEx());
		
		if(rte_st.competitions == null) 
			throw new CompetitionsNotCreatedEx();

		
		for (AdaptedCompetitorsListEntry e : v.competitorsList) {
			switch(e.lugerCompetitorType) {
			case MARRIED_COUPLE:
			case DOUBLE:
			case DOUBLE_MEN_ONLY:
			case DOUBLE_MIXED:
			case DOUBLE_WOMAN_ONLY: {
				LugerDouble n = new LugerDouble();
				n.setSystemId(e.lugerCompetitorSystemId);
				n.upper = rte_st.competitions.findLugerInListBySystemId(e.upperLugerSystemId);
				n.lower = rte_st.competitions.findLugerInListBySystemId(e.lowerLugerSystemId);
				n.setCompetitorType(e.lugerCompetitorType);
				
				out.add(n);
				
				break;
			}
			case MEN_SINGLE: {
				LugerSingle n = new LugerSingle(false);
				n.setSystemId(e.lugerCompetitorSystemId);
				n.single = rte_st.competitions.findLugerInListBySystemId(e.maleLugerSystemId);
				n.setCompetitorType(e.lugerCompetitorType);
				
				out.add(n);
				
				break;
				}
			case WOMAN_SINGLE: {
				
				LugerSingle n = new LugerSingle(true);
				n.setSystemId(e.lugerCompetitorSystemId);
				n.single = rte_st.competitions.findLugerInListBySystemId(e.femaleLugerSystemId);
				n.setCompetitorType(e.lugerCompetitorType);
				
				out.add(n);
				
				break;
			}
			case TEAM_RELAY:
				break;
			case TRAINING:
				break;
			case UNINITIALIZED_COMP:
				break;

			default:
				break;
			
			}
		}
		
		return out;
	}

}
