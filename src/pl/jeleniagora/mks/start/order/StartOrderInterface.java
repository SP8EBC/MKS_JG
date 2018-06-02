package pl.jeleniagora.mks.start.order;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Interfejs określający jakie metody muszą mieć klasy określjące kolejność startową. Większość (jak nie wszystkie) metod
 * przyjmuje jako argument referencję na obiekt klasy Competition. Jest to potrzebne zwłaszcza w przypadku kolejności FIL, 
 * która od drugiego ślizgu zależy od wyników po zakończeniu poprzedniego.
 * @author mateusz
 *
 */
public interface StartOrderInterface {
	
	/**
	 * Metoda zwraca nastepny numer startowy nastepny po wskazanym w argumencie, bądź null jeżeli wskazany numer
	 * startowy jest ostatni
	 * @param currentStartNumber
	 * @param currentCompetition
	 * @return
	 */
	public Short nextStartNumber(short currentStartNumber, Competition currentCompetition);
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition);
	public LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition);
	
	/**
	 * Metoda ma zwrcać pierwszego do startu zawodnika w aktualnym ślizgu. Ma być używana po przełączeniu ślizgu na kolejny
	 * @param currentCompetition
	 * @return
	 */
	public LugerCompetitor getFirst(Competition currentCompetition);
	
	/**
	 * Metoda ma zwracać drugiego do startu zawodnika w aktualnym ślizgu. 
	 * @param currentCompetition
	 * @return
	 */
	public LugerCompetitor getSecond(Competition currentCompetition);
	
	/**
	 * Metoda ma zwracać true jeżeli wskazany saneczkarz jest ostatni w kolejce startowej i za nim
	 * nie ma już nikogo w aktualnym ślizgu. 
	 * @return
	 */
	public boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun);


}
