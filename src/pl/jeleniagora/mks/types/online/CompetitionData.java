package pl.jeleniagora.mks.types.online;

import java.util.Vector;

/**
 * Klasa używana do aktualizacji czasów w konkurencji po każdym zakończonym ślizgu. Dla
 * ułatwienia tworzenia aplikacji za każdym razem przesyła się kompletne dane dla danej konkurencji, 
 * które są za każdym razem podmienione w bazie danych po stronie serwera
 * @author mateusz
 *
 */
public class CompetitionData {

	public CompetitionData() {
		entry = new Vector<CompetitionDataEntry>();
	}
	
	public long competitionSerialNumber;
	
	public int competitionId;
	
	public Vector<CompetitionDataEntry> entry;
}
