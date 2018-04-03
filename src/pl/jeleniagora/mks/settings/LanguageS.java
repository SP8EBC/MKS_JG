package pl.jeleniagora.mks.settings;

public class LanguageS {

	public static enum eLanguage {
		POLISH,
		SILESIAN,
		ENGLISH,
		CZECH
	};
			
	private static eLanguage language;

	public static eLanguage getLanguage() {
		return language;
	}

	public static void setLanguage(eLanguage language) {
		LanguageS.language = language;
	}
}
