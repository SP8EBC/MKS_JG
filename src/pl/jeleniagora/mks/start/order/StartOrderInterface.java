package pl.jeleniagora.mks.start.order;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pl.jeleniagora.mks.exceptions.LugerDoesntExist;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Interfejs określający jakie metody muszą mieć klasy określjące kolejność startową. Większość (jak nie wszystkie) metod
 * przyjmuje jako argument referencję na obiekt klasy Competition. Jest to potrzebne zwłaszcza w przypadku kolejności FIL, 
 * która od drugiego ślizgu zależy od wyników po zakończeniu poprzedniego. Klasy omplementujące tą klasę abstrakcyjną powinny
 * być bezstanowe i nie posiadać efektów ubocznych (side effects)!
 * @author mateusz
 *
 */
@XmlRootElement
public abstract class StartOrderInterface {
	
	/**
	 * Metoda zwraca nastepny numer startowy nastepny po wskazanym w argumencie, bądź null jeżeli wskazany numer
	 * startowy jest ostatni. Metody NIE MAJĄ brać pod uwagę czy sankarze pod numerami startowymi mają w ślizgu
	 * jakieś czasy przejazdu czy nie. Metody MAJĄ JEDYNIE zwrwcać kolejne numery startowe.
	 * @param currentStartNumber
	 * @param currentCompetition
	 * @return
	 */
	public abstract Short nextStartNumber(short currentStartNumber, Competition currentCompetition) throws LugerDoesntExist;
	public abstract Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition) throws LugerDoesntExist;
	public abstract LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition);
	
	/**
	 * Metoda ma zwrcać pierwszego do startu zawodnika w aktualnym ślizgu. Ma być używana po przełączeniu ślizgu na kolejny
	 * @param currentCompetition
	 * @return
	 */
	public abstract LugerCompetitor getFirst(Competition currentCompetition);
	
	/**
	 * Metoda ma zwracać drugiego do startu zawodnika w aktualnym ślizgu. 
	 * @param currentCompetition
	 * @return
	 */
	public abstract LugerCompetitor getSecond(Competition currentCompetition);
	
	/**
	 * Metoda ma zwracać najwyższą (najbliższą jedynki) lokatę przeliczoną po zakończeniu ostatniego
	 * ślizgu punktowanego w konkurencji, którą to lokate ma przynajmniej jeden nie jadący jeszcze zawodnik. 
	 * Metoda jest potrzebna jeżeli w drugim i kolejnych ślizgów punktowanych używało się przycisku "Omiń aktualnego" 
	 * i jest taka sytuacja że osoba o najwyższej lokacie już jechała.
	 * @param currentCompetition
	 * @param currentRun
	 * @return
	 */
	public abstract Short highestRankWithZeroRuntime(Competition currentCompetition, Run currentRun);
	
	/**
	 * Analogicznie do highestRankWithZeroRuntime ale najniższa lokata
	 * @param currentCompetition
	 * @param currentRun
	 * @return
	 */
	public abstract Short lowestRankWithZeroRuntime(Competition currentCompetition, Run currentRun);
	
	/**
	 * Metoda ma zwracać true jeżeli wskazany saneczkarz jest ostatni w kolejce startowej i za nim
	 * nie ma już nikogo w aktualnym ślizgu. Ta metoda NIE MA zawijać na początek ślizgu jeżeli nie znajdzie
	 * nikogo od wskazanego numer startowego do ostatniego (najwyższego) w konkurencji. Metoda jest potrzebna
	 * aby uwzględnić sytuacje w której wielokrotnie używano opcji "Pomiń aktualnego i przejdź do następnego.." 
	 * i innych zmieniających kolejność startową. Wtedy saneczkarz o numerze startowym niższym niż ten
	 * który normalnie powinien być ostatni, może właśnie stać się ostatnim jeżeli wszyscy przed nim już jechali
	 * @return
	 */
	public abstract boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun);
	
	/**
	 * Metoda ma sprawdzać czy wskazany numer startowy jest ostatnim numerem startowym wg zadanej kolejności startowej. 
	 * Dla kolejności uproszczonej zwróci ona true dla najwyższego numer startowego na liście. Dla kolejności FIL
	 * zwórci true dla zawodnika który w poprzednim ślizgu uzyskał najlepszy czas. W przeciwieństwie do poprzedniej
	 * metody nie są tutaj pod uwagę w ogóle brane czasy ślizgów! Metoda ma po prostu operować wyłąćznie na nrach.
	 * Używna w metodach findFirstWithoutTime
	 * @param startNum
	 * @return
	 */
	public abstract boolean checkIfLastInRun(short startNum, Competition currentCompetition);
	
//	@XmlElement(name = "StartOrder")
	public abstract String toString();


}
