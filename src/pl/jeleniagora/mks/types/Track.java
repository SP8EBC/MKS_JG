package pl.jeleniagora.mks.types;

import java.util.Vector;

/**
 * Klasa służąca do opisania poszczególnych torów sankowych. Ważne! Wiraże i bramki startowe są numerowane
 * od zera a nie od jedynki! Start numer zero to zawsze ten najwyższy, im niższy tym większy numer. Wiraże
 * są liczone tak jak to ma miejsce w realu czyli od dołu do góry. 
 * 
 * @author mateusz
 *
 */
public class Track {

	/**
	 * Unikalne id
	 */
	public int id;
	
	/**
	 * Nazwa własna jeżeli tor takową posiada.
	 */
	public String name;
	/**
	 * Lokalizacja geograficzna - najczęściej miasto.
	 */
	public String location;
	/**
	 * Współrzędne geograficzne.
	 */
	public float lat, lon;
	
	/**
	 * Liczba bramek startowych.
	 */
	public byte gateNum;
	
	/**
	 * Ustawienie na true powoduje możliwość definiowania "wirtualnych bramek" dla tego toru, co ma zastosowanie
	 * np dla Karpacza czy dla Krynicy Zdroju (góra parkowa).
	 */
	public boolean enableVirtualGates;
	
	/**
	 * Łączna ilość wiraży od najwyższego startu to mety.
	 */
	public byte curvesTotal;
	/**
	 *  Łączna długość toru od najwyższego startu do mety.
	 */
	public byte lnTotal;		
	
	/**
	 * Wektor zawierający egzemplarze klas definiujące parametry każdego "stałego", tj nie wirtualnego startu.
	 */
	public Vector<StartGate> startGates;
	
	/**
	 * Czy tor ma sztuczne lodzenie.
	 */
	public boolean hasArtificalRefrigeration;
	
	/**
	 * Ustawienie na true oznacza tor betonowy - w domyśle ze sztucznym lodzeniem, false oznacza tor plastikowy albo
	 * drewniany
	 */
	public boolean isMadeFromConcrete;
}
