package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa będąca adapterem pomiędzy abstrakcyjnym typem LugerCompetitor a typem pomocniczym który jest bezpośrednio ciepany
 * i odczytywany do/z XMLa
 * @author mateusz
 *
 */
public class LugerCompetitorAdapter extends XmlAdapter<LugerCompetitorAdapter.AdaptedCompetitorLuger, LugerCompetitor> {

	public static class AdaptedCompetitorLuger {

	}

	@Override
	public AdaptedCompetitorLuger marshal(LugerCompetitor v) throws Exception {
		return null;
	}

	@Override
	public LugerCompetitor unmarshal(AdaptedCompetitorLuger v) throws Exception {
		return null;
	}

}
