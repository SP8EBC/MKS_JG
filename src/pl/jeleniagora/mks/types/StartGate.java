package pl.jeleniagora.mks.types;

/**
 * Klasa używana do mapowania bramki na tor na którym się ona znajduje i na jej właściwości. Ważne jest aby pamiętać
 * że braki startowe i wiraże sa liczone od zera a nie od jedynki. W tym przypadku id jest to id systemowe 
 */
public class StartGate {

	/**
	 * Unikalne id systemowe.
	 */
	public int id;
	
	/**
	 * Numer bramki na torze, odliczany od zera. Co do zasaady bramka numer zero jest to bramka najwyższa (start męski)
	 * każda niższa ma odpowiednio wyższy numer. 
	 */
	public int gateNo;
	
	/**
	 * Tor na którym znajduje się ta bramka.
	 */
	public Track track;
	
	/**
	 * Rodzaj startu określający czy jest to start męski, damski, juniorski czy młodzików.
	 */
	public StartGateType type;
	
	/**
	 * Długość trasy do przejechania z tej bramki.
	 */
	public short ln;
	
	/**
	 * Ilość wiraży z tej bramki.
	 */
	public short curves;
	
}
