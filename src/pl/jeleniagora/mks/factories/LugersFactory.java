package pl.jeleniagora.mks.factories;

import java.time.LocalDate;
import java.util.HashMap;

import pl.jeleniagora.mks.types.Club;
import pl.jeleniagora.mks.types.Luger;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerSingle;
import pl.jeleniagora.mks.types.StartGate;

/**
 * Klasa o nieco głupkowatej nazwie brzmiącej po polsku po prostu jako "fabryka saneczkarzy", służy do generowania obiektów klas implementujących
 * interfejs "LugerCompetitor". Nazwa jest może i nieco dziwna ale odpowiada systematyce przyjętej powszechnie w technologii java. Statyczne
 * metody składowe mogą albo odczytywać dane z plików XML czy bazy danych albo tworzyć całkowicie nowe obiekty na podstawie danych wejściowych.
 * 
 * TODO: W zasadzie to tutaj powinno być Dependency Injection ze springa bo każdy saneczkarz powinien być w MKS_JG singletonem.
 * 
 * @author mateusz
 *
 */
public class LugersFactory {

	
	public static LugerCompetitor createNewLugerSingleFromName(String name, String surname, boolean isFermale, LocalDate birthday, Club club) {
			LugerSingle single = new LugerSingle(isFermale);
			
			single.single = new Luger();
			
			Luger s = single.single;
			
			s.birthDate = birthday;
			s.name = name;
			s.surname = surname;
			s.club = club;
			s.runsCounters = new HashMap<StartGate, Short>();

			
			return single;
	}
}
