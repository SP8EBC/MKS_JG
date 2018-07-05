package pl.jeleniagora.mks.types;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Klasa definiująca saneczkarza i wszystkie jego parametry takie jak Imię, Nazwisko, przynależność klubowa, referencję do 
 * zawodów i treningów w których brał udiał itp. Wszelka zbieżność do niemieckiej broni krótkiej strzelającej amunicją 9 x 19mm parabellum
 * jest tu przypadkowa i niezamierzona. Co ciekawe ta potoczna nazwa, która przylgneła do pistoletu P08 Parabellum
 * wzięła się od nazwiska konstruktora, Georga Lugera. Luger był oczywiście Niemcem, natomiast "Luger" jako saneczkarz jest to naturalnie
 * słowo angielskie. Sama nazwa "Luge" określająca sport sankowy jest zaś pochodzenia francuskiego, fakt zaskakujący o tyle o ile francuzi
 * raczej nie są postrzegani jako naród o nadzwyczajnej odwadze. 
 * 
 * @author mateusz
 *
 */
public class Luger {

	public int id;
	
	/**
	 * Generowane losowo pole określające systemowy identyikator saneczkarza
	 */
	@SuppressWarnings("unused")
	private long systemId;
	
	public void generateSystemId() {
		 this.systemId = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);

	}
	
	public long getSystemId() {
		return this.systemId;
	}
	
	public void setSystemId(long in) {
		this.systemId = in;
	}
	
	@XmlTransient
	public String name, surname;
	
	@XmlTransient
	public LocalDate birthDate;
	
	@XmlTransient
	public Club club;
	
	public String email;

	/**
	 * Mapa zawierająca liczniki ślizgów dla każdego toru i bramki startowej z osobna.
	 */
	@XmlTransient
	public Map<StartGate, Short> runsCounters;
	
	@Override
	public String toString() {
		return this.name + " " + this.surname;
	}
	
	public Luger() {
		this.generateSystemId();
	}
}
