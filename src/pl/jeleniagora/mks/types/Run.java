package pl.jeleniagora.mks.types;

import java.time.LocalTime;
import java.util.Map;

/**
 * Klasa używana do przechowywania wyników dla poszczególnych ślizgów. 
 * @author mateusz
 *
 */
public class Run {
	
	/**
	 * Mapa łącząca zawodników z ich wynikami dla konkretnej konkurencji
	 */
	Map<Luger, LocalTime> run;
}
