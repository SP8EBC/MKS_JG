package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

/**
 * Klasa będąca adapterem pomiędzy abstrakcyjnym typem LugerCompetitor a typem pomocniczym który jest bezpośrednio ciepany
 * i odczytywany do/z XMLa. Nleży bezwzględnie zapamiętać, że adapter ten NIE będzie używany do konwersji obiektu Vector<Luger>
 * znajdującego się bezpośrednio w klasie Competitions. Poniższe będzie używane tylko w odniesieniu to wszelakich Map, List, 
 * wektorów itp. zawierających abstrakcyjny typ LugerCompetitor. Z typu LugerCompetitor zwracane będzie jedynie systemoweId
 * w postaci 64 bitowej liczby całkowitej ze znakiem. Podejście takie ma zapewnić oszczędzanie miejsca w pliku XML, przez
 * uniknięcie powtarzania tych samych danych o zawodniku (personalia itp) w n różnych miejscach 
 * @author mateusz
 *
 */
public class LugerCompetitorAdapter extends XmlAdapter<LugerCompetitorAdapter.AdaptedCompetitorLuger, LugerCompetitor> {

	public static class AdaptedCompetitorLuger {
		
		@XmlAttribute
		public long lugerSystemId;			// LugerSingle
		
		@XmlAttribute
		public long bottomLugerSystemId;	// Dwójka sankowa - dół
		
		@XmlAttribute
		public long upperLugerSystemId;		// Dwójka sankowa - góra
		
		@XmlAttribute
		public long maleLugerSystemId;		// Sztafeta albo drużyna
		
		@XmlAttribute
		public long femaleSystemId;			// jw
		
		@XmlAttribute		
		public long doubleSystemId;			// jw
		
		
	}

	@Override
	public AdaptedCompetitorLuger marshal(LugerCompetitor v) throws Exception {
		
		AdaptedCompetitorLuger adaptedCompetitorLuger = new AdaptedCompetitorLuger();
		
		if (v instanceof LugerSingle) {
			adaptedCompetitorLuger.lugerSystemId = ((LugerSingle)v).single.getSystemId();
			return adaptedCompetitorLuger;
		}
		if (v instanceof LugerDouble) {
			adaptedCompetitorLuger.bottomLugerSystemId = ((LugerDouble)v).lower.getSystemId();
			adaptedCompetitorLuger.upperLugerSystemId = ((LugerDouble)v).upper.getSystemId();
			return adaptedCompetitorLuger;


		}
		
		return null;
	}

	@Override
	public LugerCompetitor unmarshal(AdaptedCompetitorLuger v) throws Exception {
		return null;
	}

}
