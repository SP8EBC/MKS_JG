package pl.jeleniagora.mks.types;

import java.time.LocalDate;
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
}
