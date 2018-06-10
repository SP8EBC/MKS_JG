package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.CompetitionTypes;

public class CompetitionTypesAdapter extends XmlAdapter<String, CompetitionTypes> {

	@Override
	public String marshal(CompetitionTypes v) throws Exception {
		switch (v) {
		case MEN_SINGLE: 
			return "MEN_SINGLE";
		case DOUBLE:
			return "DOUBLE";
		case DOUBLE_MEN_ONLY:
			return "DOUBLE_MEN_ONLY";
		case DOUBLE_MIXED:
			return "DOUBLE_MIXED";
		case DOUBLE_WOMAN_ONLY:
			return "DOUBLE_WOMAN_ONLY";
		case TEAM_RELAY:
			return "TEAM_RELAY";
		case TRAINING:
			return "TRAINING";
		case UNINITIALIZED_COMP:
			return "UNINITIALIZED_COMP";
		case WOMAN_SINGLE:
			return "WOMAN_SINGLE";
		default:
			return null;
		}
	}

	@Override
	public CompetitionTypes unmarshal(String v) throws Exception {
		CompetitionTypes out = CompetitionTypes.UNINITIALIZED_COMP;
		
		switch (v) {
		case "MEN_SINGLE": out = CompetitionTypes.MEN_SINGLE; break;
		case "DOUBLE": out = CompetitionTypes.DOUBLE; break;
		case "DOUBLE_MEN_ONLY": out = CompetitionTypes.DOUBLE_MEN_ONLY; break;
		case "DOUBLE_MIXED": out = CompetitionTypes.DOUBLE_MIXED; break;
		case "DOUBLE_WOMAN_ONLY": out = CompetitionTypes.DOUBLE_WOMAN_ONLY; break;
		case "TEAM_RELAY": out = CompetitionTypes.TEAM_RELAY; break;
		case "TRAINING": out = CompetitionTypes.TRAINING; break;
		case "UNINITIALIZED_COMP": out = CompetitionTypes.UNINITIALIZED_COMP; break;
		case "WOMAN_SINGLE": out = CompetitionTypes.WOMAN_SINGLE; break;
		
		
		}
		
		return out;
	}

}
