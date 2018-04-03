package pl.jeleniagora.mks.settings;

public class GeneralS {

	/**
	 * Ustawione na true powoduje wyświetlone różnego typu konkurencji dwójek (dwójki męskie, dwójki kobiece,
	 * dwójki mieszane), zamiast po prostu wyświetlać "dwójki" bez żadnego rozróżnienia
	 */
	private static boolean showDiffDoubles;

	public static boolean isShowDiffDoubles() {
		return showDiffDoubles;
	}

	public static void setShowDiffDoubles(boolean showDiffDoubles) {
		GeneralS.showDiffDoubles = showDiffDoubles;
	}
	
}
