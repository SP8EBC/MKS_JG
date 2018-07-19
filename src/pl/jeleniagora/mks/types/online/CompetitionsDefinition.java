package pl.jeleniagora.mks.types.online;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Klasa używana przy definiowaniu nowych zawodów do wyświetlenia w wynikach online
 * @author mateusz
 *
 */
public class CompetitionsDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8428274238689168284L;
	
	public long competitionsUniqId;
	
	/**
	 * Wyświetlana nazwa zawodów
	 */
	public String competitionsName;
	
	/**
	 * Wyświetlana data rozgrywania zawodów
	 */
	public LocalDate competitionsDate;
	
	/**
	 * String określający miejsce rozgrywania zawodów
	 */
	public String location;
	
	public String trackName;
	
	/**
	 * Ścieżka do pierwszego loga (lewy górny róg)
	 */
	public String logo1;
	
	/**
	 * Ścieżka do drugiego loga (górny drógi róg)
	 */
	public String logo2;
	
	/**
	 * Imiona i Naziwsko sędziów
	 */
	public String judge1;
	public String judge2;
	public String judge3;
	
	/**
	 * Nazwa organizatora
	 */
	public String organizer;
	
	/**
	 * Liczba konkurencji
	 */
	public int compCount;
	
}
