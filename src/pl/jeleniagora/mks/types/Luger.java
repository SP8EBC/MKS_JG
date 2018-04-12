package pl.jeleniagora.mks.types;

import java.time.LocalDate;
import java.util.Map;

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
	
	public String name, surname;
	
	public LocalDate birthDate;
	
	public String club;

	/**
	 * Mapa zawierająca liczniki ślizgów dla każdego toru i bramki startowej z osobna.
	 */
	public Map<StartGate, Short> runsCounters;
}
