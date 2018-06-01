package pl.jeleniagora.mks.types;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Vector;

/**
 * Klasa definiująca całościowo zawody. Mała uwaga językowa: Competition -> Konkurecja ; Competitions -> Zawody
 * @author mateusz
 *
 */
public class Competitions {
	
	/**
	 * Nazwa zawodów
	 */
	public String name;
	
	/**
	 * Data kiedy odbywają się zawody
	 */
	public LocalDate date;

	/**
	 * Wektor z wszystkimi konkurencjami w tych zawodach
	 */
	public Vector<Competition> competitions;
	
	/**
	 * Tor na którym odbywają się zawody
	 */
	public Track track;
	
	/**
	 * Mapowanie konkurencji do bramki startowej
	 */
	public Map<Competition, StartGate> competitionToStartGateMapping;
	
	public String toString() {
		DateTimeFormatter fmtr = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		String ds;
		
		if (date != null)
			ds = date.format(fmtr);
		else
			ds = new String("");
		
		if (name != null) {
			int nameLn = name.length();
			if (nameLn > 8) nameLn = 8;
			return name.substring(0, nameLn) + "_" + ds;
		}
		else return new String("");
	}
	
	public Competitions(String _name, LocalDate _date) {
		name = _name;
		date = _date;
	}
}
