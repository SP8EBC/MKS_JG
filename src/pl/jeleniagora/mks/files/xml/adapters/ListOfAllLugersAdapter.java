package pl.jeleniagora.mks.files.xml.adapters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Club;
import pl.jeleniagora.mks.types.Luger;

@Component
public class ListOfAllLugersAdapter extends XmlAdapter<ListOfAllLugersAdapter.AdaptedList, List<Luger>> {

	static RTE_ST rte_st;
	
	@Autowired
	public void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	public static class AdaptedList { 
		@XmlElement(name = "luger")
		List<LugeRacer> adapted = new ArrayList<LugeRacer>();
	}
	
	/**
	 * Klasa ta została nazwana może i w nieco głupawy sposób ale chodzi o odróżnienie jej od "nadrzęnej" i "globalnej" klasy Luger która choć 
	 * bardzo podobna do tej, jest używana do nieco innych rzeczy w innych miejscach. LugerRacer ma służyć tylko i wyłącznie adapterowi XML dla 
	 * typu List<LugerCompetitor> Za wszelką cenę chcę uniknąć w nazewnictwa słowa 'athlete' które kojarzy mi się jedoznacznie negatywnie 
	 * z red bullem oczywiście..
	 * 
	 * @author mateusz
	 *
	 */
	public static class LugeRacer {
		public long systemId;
		
		public String name, surname;
		
		@XmlJavaTypeAdapter(LocalDateAdapter.class)
		public LocalDate birthDate;
		
		public Club club;
		
		@XmlElement(required = false, nillable = true )
		public String email;

	}

	@Override
	public AdaptedList marshal(List<Luger> arg0) throws Exception {
		AdaptedList out = new AdaptedList();
		
		for (Luger e : arg0) {
			
			LugeRacer entry = new LugeRacer();
			
			entry.birthDate = e.birthDate;
			entry.club = e.club;
			entry.email = e.email;
			entry.name = e.name;
			entry.surname = e.surname;
			entry.systemId = e.getSystemId();
			
			out.adapted.add(entry);
		}
		
		return out;
	}

	@Override
	public List<Luger> unmarshal(AdaptedList arg0) throws Exception {
		
		List<Luger> out = new ArrayList<Luger>();
		
		for (LugeRacer e : arg0.adapted) {
			Luger n = new Luger();
			
			n.birthDate = e.birthDate;
			n.club = e.club;
			n.email = e.email;
			n.surname = e.surname;
			n.name = e.name;
			n.setSystemId(e.systemId);
			
			out.add(n);
			
			rte_st.competitions.listOfAllLugersInThisCompetitions.add(n);
			
		}
		
		return out;
		
	}

}
