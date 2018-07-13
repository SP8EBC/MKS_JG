package pl.jeleniagora.mks.start.order;

import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

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
		
		Short returnValue = null;
		
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

			// wyciąganie wszystkich sankarzy z numerem startowym przekazanym do metody jako aktualny (sprawdzenie ex-aequo)
			Vector<LugerCompetitor> exa = currentCompetition.returnLugersWithThisRank(rankOfCurrentStartNum, true);
			
			// jeżeli miejsce aktualnego sankarza jest ex-aequo z innym to trzeba wybrać następnego ex-equo z mniejszym numerem startowym
			if (currentCompetition.checkIfExAequo(rankOfCurrentStartNum) > 1) {
				
				// przeszukiwanie wektora z miejscami ex-aequo w poszukiwaaniu pierwszego zawodnika z nr startowym mniejszym niż aktualny
				for (LugerCompetitor elem : exa) {
					// metoda 'returnLugersWithThisRank' zwraca zawodników posortowanych po nr startowych malejąco
					if (elem.getStartNumber() < currentStartNumber) {
						returnValue = elem.getStartNumber();
						break;
					}
				}
				
				// jeżeli returnValue jest w dalszym ciągu nullem to ozncza, że ten był ostatnim ex-aequo i trzeba przeskoczyć do kolejnego
				// sankarza, to jest do tego który ma kolejną niższa lokatę
				if (returnValue == null) {
					
					// entry set zawierający wszystkich sankarzy w konkurencji
					Set<Entry<LugerCompetitor, Short>> set = currentCompetition.ranks.entrySet();
					
					// zamienianie entryset na nieposortowny wektor, który potem będzie sortowany po lokatach malejąco
					Vector<Entry<LugerCompetitor, Short>> rankVector = new Vector<Entry<LugerCompetitor, Short>>(set);
					
					// sortowanie wektora malejąco po lokatach
					rankVector.sort((Entry<LugerCompetitor, Short> e1, Entry<LugerCompetitor, Short> e2) -> {
						// SORTOWANIE ROSNĄCE:
						// lambda ma zwrócić wartość dodatnią jeżeli e1 jest większe niż e2
						// lambda ma zwrócić zero jeżeli są takie same
						// lambda ma zwrócić wartość ujemną jeżeli e1 jest mniejsze niż e2 
						// --- Żeby posortować malejąco trzeba odwrócić tą logikę ---
						return e2.getValue() - e1.getValue();	 // TODO: lambda!!
					});
					
					// pętla po wszystkich elementach z posortownego malejąco wektora
					for (Entry<LugerCompetitor, Short> e : rankVector) {
						// jeżeli lokata aktualnie sprawdzanego numeru startowego jest mniejszy niż sankarz o nrze
						// startowym przekazanym jako parametr metody, to zwróc właśnie tego aktualnie sprawdzanego.
						if (e.getValue() < rankOfCurrentStartNum) {
							returnValue = e.getKey().getStartNumber();
							break;
						}
					}
					
					// jeżeli returnValue w dalszym ciągu jest nullem to onacza to, że ten zawodnik miał pierwszą lokatę i to jest już koniec
					if (returnValue == null)
						return null;
				}
			}
			else {
				// jeżeli to nie jest miejsce ex-aequo to po prostu trzeba zwórić nr startowy sankarza który zajął lokatę o jedną mniejszą
				
				// ... chyba że do metody przekazano numer startowy sankarza który był pierwszy, wtedy nie ma już żadnego następnego
				if (rankOfCurrentStartNum == 1)
					return null;
				
				// jeżeli sankarz przekazany do metody nie był pierwszy, to należy sprawdzić czy kolejna miejsza lokata nie jest ex-aequo
				Vector<LugerCompetitor> nexa = currentCompetition.returnLugersWithThisRank((short) (rankOfCurrentStartNum - 1), false); 
				
				// jeżeli nie jest ex-aequo to po prostu zwróć numer startowy zawodnika który ma tą kolejną lokatę
				if (nexa.size() == 1) {
					returnValue = nexa.get(0).getStartNumber();
				}
				// jeżeli jednak kolejna mniejsza lokata jest ex-aequo to nalezy zwrócić z niej zawodnika o najwyższym nrze startowym
				else {
					returnValue = nexa.get(nexa.size() - 1).getStartNumber(); // który zawsze będzie na końcu tego wektora
				}
			}
		}
		else {
			// jeżeli należy tu użyć kolejności prostej to po prostu użyj metod z klasy SimpleOrder
			SimpleOrder simple = new SimpleOrder();
			
			returnValue = simple.nextStartNumber(currentStartNumber, currentCompetition);
		}
		
		return returnValue;
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
