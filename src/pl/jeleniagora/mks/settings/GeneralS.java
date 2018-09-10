package pl.jeleniagora.mks.settings;

public class GeneralS {

	/**
	 * Ustawione na true powoduje wyświetlone różnego typu konkurencji dwójek (dwójki męskie, dwójki kobiece,
	 * dwójki mieszane), zamiast po prostu wyświetlać "dwójki" bez żadnego rozróżnienia
	 */
	private static boolean showDiffDoubles;
	
	private static boolean partialRanksRunOnly = false;
	
	private static String webServiceHostAddr = "http://luge.pl:8080/MKS_JG_ONLINE";

	public static boolean isShowDiffDoubles() {
		return showDiffDoubles;
	}

	public static void setShowDiffDoubles(boolean showDiffDoubles) {
		GeneralS.showDiffDoubles = showDiffDoubles;
	}

	public static boolean isPartialRanksRunOnly() {
		return partialRanksRunOnly;
	}

	public static void setPartialRanksRunOnly(boolean partialRanksRunOnly) {
		GeneralS.partialRanksRunOnly = partialRanksRunOnly;
	}

	public static String getWebServiceHostAddr() {
		return webServiceHostAddr;
	}

	public static void setWebServiceHostAddr(String webServiceHostAddr) {
		GeneralS.webServiceHostAddr = webServiceHostAddr;
	}
	
}
