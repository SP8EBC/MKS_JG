package pl.jeleniagora.mks.start.order;

import java.time.LocalTime;

import javax.xml.bind.annotation.XmlElement;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa generuje "zwykłą" kolejność startową tj. zgodną z wylosowanymi nrami startowymi rosnąco
 * @author mateusz
 *
 */
public class SimpleOrder extends StartOrderInterface { 

	public SimpleOrder() {
		
	}
	
	@Override
	public String toString() {
		return "SIMPLE_ORDER";
	}

	@Override
	public Short nextStartNumber(short currentStartNumber, Competition currentCompetition) {
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		short lastStartNumber = currentCompetition.getLastStartNumber();
		
		do {
			++currentStartNumber; // zwiększanie numeru startowego o jeden
			
			// sprawdzenie czy numer startowy o jeden większy niż aktualny w ogóle istnieje
			if (currentCompetition.invertedStartList.containsKey(currentStartNumber)) {
				return currentStartNumber; // jeżeli istnieje to zwróć właśnie ten jako następny
			}
		
			// pętla wykonuje się do momentu w którym nie dojdzie się do końca lity startowej, czyli najwyższego nru startowego
		} while(currentStartNumber < lastStartNumber);
		
		return null;
	}

	@Override
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		short lastStartNumber = currentCompetition.getLastStartNumber();
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		short currentSN = currentStartNumber.getStartNumber();
		
		if (currentSN < lastStartNumber)
			/* Jeżeli na liście startowej jest jeszcze jakis sankarz to po prostu zwróć kolejny numer startowy */
			return ++currentSN;
		else
			/* W przeciwnym razie zwróc null */
			return null;
	}

	@Override
	//TODO: poprawić!!
	public LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		short lastStartNumber = currentCompetition.getLastStartNumber();
		int startlistSize = currentCompetition.invertedStartList.size(); // ilość elementów w liście startowej czyli liczba zaneczkarzy
		short currentSN = currentStartNumber.getStartNumber();
		
		LugerCompetitor returnValue = null;
		
		if (currentSN < lastStartNumber) {
			returnValue = currentCompetition.invertedStartList.get((short)(currentSN +1));
		}
		else {
			returnValue = null;
		}
		
		return returnValue;
	}

	@Override
	public LugerCompetitor getFirst(Competition currentCompetition) {
		
		LugerCompetitor returnValue = null;
		int numberOfLugers = currentCompetition.invertedStartList.size();
		
		// pętla zaczyna od pierwszego numeru startowego (numer 1) i idzie w góre
		for (int i = 1; i <= numberOfLugers; i++) {
			returnValue =  currentCompetition.invertedStartList.get((short)i);
			
			// jeżeli wyciągnięty po tym numerze startowym istnieje to nalezy go zwrócić
			// jako pierwszego znalezionego
			if (returnValue != null)
				break;
			
		}
		
		return returnValue;
	}

	@Override
	public LugerCompetitor getSecond(Competition currentCompetition) {
		
		LugerCompetitor returnValue = null;
		//int numberOfLugers = currentCompetition.invertedStartList.size();

		// pętla zaczyna od zawodnika który ma numer startowy o jeden większ niż pierwszy
		for (short i = (short) (this.getFirst(currentCompetition).getStartNumber() + 1); i <= currentCompetition.getLastStartNumber(); i++) {
			returnValue =  currentCompetition.invertedStartList.get((short)i);
			
			// jeżeli wyciągnięty po tym numerze startowym istnieje to nalezy go zwrócić
			// jako pierwszego znalezionego
			if (returnValue != null)
				break;
			
		}
		
		return currentCompetition.invertedStartList.get((short)(2));
	}

	@Override
	public boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun) {
		// uwaga mapa startList może zawierać zarówno zawodników z numerami startowymi jak również tych bez nich(wartość 0!)
		
		short lastStartNumber = currentCompetition.getLastStartNumber();
		short firstStartNumber = currentCompetition.getFirstStartNumber();
		
		int numberOfComp = currentCompetition.invertedStartList.size(); // wcześniej było start list
		short startNumber = in.getStartNumber();	// numer startowy sankarza do sprawdzenia czy jest ostatni
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		boolean output = true;
		
		for (int i = startNumber; i < lastStartNumber; i++) {
			//  
			LocalTime v = currentRun.totalTimes.get(in);	// wyciąganie czasów ślizgu dla każdego kolejnego za tym to sprawdzenia
			
			if (v != null && v.equals(zero)) {
				output = false;
				break;
			}
		}
		
		return output;
	}

	@Override
	public boolean checkIfLastInRun(short startNum, Competition currentCompetition) {
		
		short lastStartNumber = currentCompetition.getLastStartNumber();
		short firstStartNumber = currentCompetition.getFirstStartNumber();
		
		if (startNum == lastStartNumber)
			return true;
		
		return false;
	}
	
}
