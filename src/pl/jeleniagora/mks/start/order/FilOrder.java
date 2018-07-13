package pl.jeleniagora.mks.start.order;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa która będzie generowała "koszerną" tj zgodną z regulaminem sportowym FIL kolejność startową. 
 * W pierwszym śligu kolejność wyznaczają numery startowe. W kolejnych ślizgach kolejność startowa
 * jest wyznaczona przez miejsca zajęte w przez zawodnikach po zakończeniu poprzedniego w kolejności odwrotnej. 
 * Na przykład w ślizgu punktowanym nr 2 jako pierwszy będzie jechał ten kto zajął ostatnie miejsce w ślizgu 
 * punktowanym nr 1. W przypadku miejsc ex-aeqo pierwszy jedzie ten o najniższym nrze startowym
 * @author mateusz
 *
 */
public class FilOrder  extends StartOrderInterface {
	
	@Override
	public Short nextStartNumber(short currentStartNumber, Competition currentCompetition) {
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"
		
		if (applyFilOrder) {
			// obiekt klasy LugerCompetitor dla aktualnego numeru startowego - wyciąganie sankarza 
			LugerCompetitor competitorForCurrentStartNum = currentCompetition.invertedStartList.get(currentStartNumber);
			short rankOfCurrentStartNum = currentCompetition.ranks.get(competitorForCurrentStartNum);
		
		}
		else {
			// jeżeli należy tu użyć kolejności prostej to po prostu użyj metod z klasy SimpleOrder
			SimpleOrder simple = new SimpleOrder();
			
			return simple.nextStartNumber(currentStartNumber, currentCompetition);
		}
		
		return null;
	}

	@Override
	public Short nextStartNumber(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		return null;
	}

	@Override
	public LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		return null;
	}

	@Override
	public LugerCompetitor getFirst(Competition currentCompetition) {
		return null;
	}

	@Override
	public LugerCompetitor getSecond(Competition currentCompetition) {
		return null;
	}

	@Override
	public boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun) {
		return false;
	}

	@Override
	public boolean checkIfLastInRun(short startNum, Competition currentCompetition) {
		return false;
	}

	@Override
	public String toString() {
		return "FIL_ORDER";
	}

}
