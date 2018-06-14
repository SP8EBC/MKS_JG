package pl.jeleniagora.mks.files.xml.adapters;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import pl.jeleniagora.mks.types.Club;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

public class ListOfAllCompetitorsAdapter extends XmlAdapter<ListOfAllCompetitorsAdapter.AdaptedList, List<LugerCompetitor>> {

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
		
		public String email;

	}

	@Override
	public AdaptedList marshal(List<LugerCompetitor> arg0) throws Exception {
		AdaptedList out = new AdaptedList();
		
		for (LugerCompetitor e : arg0) {
			
			if (e instanceof LugerSingle) {
				LugeRacer entry = new LugeRacer();
				
				entry.birthDate = ((LugerSingle)e).single.birthDate;
				entry.club = ((LugerSingle)e).single.club;
				entry.email = ((LugerSingle)e).single.email;
				entry.name = ((LugerSingle)e).single.name;
				entry.surname = ((LugerSingle)e).single.surname;
				entry.systemId = ((LugerSingle)e).single.getSystemId();
				
				out.adapted.add(entry);
			}
			else if (e instanceof LugerDouble) {
				// jeżeli jest to dwójka sankowa to trzeba dociepać osobno tego co jest na górzae
				// i tego co jest na dole
				
				LugeRacer onTop = new LugeRacer();
				LugeRacer onBottom = new LugeRacer();
				
				onTop.birthDate = ((LugerDouble)e).upper.birthDate;
				onTop.club = ((LugerDouble)e).upper.club;
				onTop.email = ((LugerDouble)e).upper.email;
				onTop.name = ((LugerDouble)e).upper.name;
				onTop.surname = ((LugerDouble)e).upper.surname;
				onTop.systemId = ((LugerDouble)e).upper.getSystemId();

				onBottom.birthDate = ((LugerDouble)e).lower.birthDate;
				onBottom.club = ((LugerDouble)e).lower.club;
				onBottom.email = ((LugerDouble)e).lower.email;
				onBottom.name = ((LugerDouble)e).lower.name;
				onBottom.surname = ((LugerDouble)e).lower.surname;
				onBottom.systemId = ((LugerDouble)e).lower.getSystemId();

				out.adapted.add(onBottom);
				out.adapted.add(onTop);
			}
			else;
		}
		
		return out;
	}

	@Override
	public List<LugerCompetitor> unmarshal(AdaptedList arg0) throws Exception {
		return null;
	}

}
