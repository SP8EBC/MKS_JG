package pl.jeleniagora.mks.settings;

public class DisplayS {

	/**
	 * Ustawione na true powoduje wyświetlanie na liście wyników i w raportach wszystkich cyfr znaczących w milisekundach.
	 * Czyli jeżeli saneczkarz uzyskal czas przejazdy 44 sekundy i 87 milisekund to będzie to przy true będzie wyświetlane
	 * jako 44.087 a jeżeli będzie na false to wyświetli jako 44.87 W przypadku sekund zero zawsze jest wyświetlane
	 */
	private static boolean showAllTimeDigits;
	
	private static boolean inhibitMessageAtEndOfRun;
	
	public final static int columnOffset = 6;	// zmiana po dodaniu lokaty w bierzącym ślizgu

	public static boolean isShowAllTimeDigits() {
		return showAllTimeDigits;
	}

	public static void setShowAllTimeDigits(boolean showAllTimeDigits) {
		DisplayS.showAllTimeDigits = showAllTimeDigits;
	}

	public static boolean isInhibitMessageAtEndOfRun() {
		return inhibitMessageAtEndOfRun;
	}

	public static void setInhibitMessageAtEndOfRun(boolean inhibitMessageAtEndOfRun) {
		DisplayS.inhibitMessageAtEndOfRun = inhibitMessageAtEndOfRun;
	}
	
}
