package pl.jeleniagora.mks.start.order;

import java.util.Map.Entry;
import java.time.LocalTime;
import java.util.Set;
import java.util.Vector;

import pl.jeleniagora.mks.exceptions.LugerDoesntExist;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa która będzie generowała "koszerną" tj zgodną z regulaminem sportowym FIL kolejność startową. 
 * W pierwszym śligu kolejność wyznaczają numery startowe od pierwszego rosnąco. W kolejnych ślizgach kolejność 
 * startowa jest wyznaczona przez miejsca zajęte przez zawodników po poprzednio zakończonym ślizgu/zjeździe 
 * w kolejności odwrotnej - jedzie najpierw ostatni a potem kolejni w stronę tego który zajął pierwsze miejsce.
 * Na przykład w ślizgu punktowanym nr 2 jako pierwszy będzie jechał ten kto zajął ostatnie miejsce w ślizgu 
 * punktowanym nr 1. W przypadku miejsc ex-aeqo pierwszy jedzie ten o najwyższym nrze startowym a potem 
 * kolejno ci o coraz niższych (coraz bliższych jedynki) numerach startowych
 * @author mateusz
 *
 */
public class FilOrder  extends StartOrderInterface {
	
	@Override
	public Short nextStartNumber(short currentStartNumber, Competition currentCompetition) throws LugerDoesntExist {
		
		Short returnValue = null;
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"
		
		if (applyFilOrder) {
			if (currentStartNumber == 0) {
				// jeżeli to metody przekazano numer startowy 0 to oznacza to że dotarło się
				// do końca konkurencji i metoda jest wywoływana z UpdateCurrentAndNextLuger.findFirstWithoutTime
				// gdzie służy do zwracania kolejnych numerów startowych do sprawdzenia dla nich czasów przejazdu
				returnValue = this.getFirst(currentCompetition).getStartNumber();
				
				return returnValue;
			}
			
			// obiekt klasy LugerCompetitor dla aktualnego numeru startowego - wyciąganie sankarza 
			LugerCompetitor competitorForCurrentStartNum = currentCompetition.invertedStartList.get(currentStartNumber);
			
			// jeżeli z odwróconej start listy zwrócono null oznacza to, że do metody przekazano numer startowy
			// który nie istnieje 
			if (competitorForCurrentStartNum == null) {
				throw new LugerDoesntExist();
			}
			
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
				
				// jeżeli kolejna lokata za sprawdzanym numerem nie istnieje to oznacza to zbliżanie się do miejsca ex-aeqo
				if (nexa == null) {
					// tu należy odszukać kolejną lokatę po tej zajmowanej przez aktualnie sprawdzany nr startowy
					int rank = rankOfCurrentStartNum - 1;
					
					// pętla będzie po koleji zmniejszała lokatę idąc coraz bardziej pierwszego miejsca i sprawdza czy
					// istnieje zawodnik o tej lokacie
					while (rank > 0) {
						Vector<LugerCompetitor> cmptrs = currentCompetition.returnLugersWithThisRank((short) rank, false);
						
						if (cmptrs != null) {
							// jeżeli referencja do wektora jest różna od nulla to znaczy że są zawodnicy o tej lokacie
							// istnieją i można ten wektor przekazać do dalszego procesowania
							
							nexa = cmptrs;
							break;
						}
						
						rank--;
					}
					
					// jeżeli pętla dotarła do zera i nie znalazła żadnych zawodników to oznacza to, że ma on najwyższą
					// lokatę. Może to być jednak postrzegane jako błąd w strukturze danych, bo jeżeli nie jest to miejsce
					// ex-aequo i nie jest to jednocześnie miejsce nr 1 to taka sytuacja nie powinna się w ogóle znaleźdź
					//
					// TODO: wymyśleć co tu się powinno stać
					if (rank == 0) {
						return null;
					}
				}
				
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
		short sn = currentStartNumber.getStartNumber();
		
		try {
			return this.nextStartNumber(sn, currentCompetition);
		} catch (LugerDoesntExist e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LugerCompetitor nextStartLuger(LugerCompetitor currentStartNumber, Competition currentCompetition) {
		short sn = currentStartNumber.getStartNumber();

		short nextSn = 0;
		try {
			nextSn = this.nextStartNumber(sn, currentCompetition);
		} catch (LugerDoesntExist e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentCompetition.invertedStartList.get(nextSn);
	}

	@Override
	public LugerCompetitor getFirst(Competition currentCompetition) {
		LugerCompetitor returnValue = null;
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"
		
		// jeżeli nalezy tu zastosować kolejność FIL to trzeba odszukać zawodnika który zajął ostanią lokatę po 
		// ostatnio zakończonym ślizgu / zdjeździe
		if (applyFilOrder) {
			
			short lowestRank = 0;
			
			// określanie jaka jest ostatnia lokata
			for (Entry <LugerCompetitor, Short> e : currentCompetition.ranks.entrySet()) {
				// pomijaj w analizie wszytkie lokaty 999, czyli zawodników DNF/DSQ/DNS
				if (e.getValue().equals(new Short((short) 999))) {
					continue;
				}
				
				// jeżeli miejsce (lokata) aktualnie sprawdzanego sankarza jest niższe niż ostatnie najniższe
				// to zaktalizuj odpowiednią zmienną
				if (e.getValue() > lowestRank)
					lowestRank = e.getValue();
			}
			
			// sprawdzanie czy ostatnia lokate nie jest ex-aequo. wektor będzie wrócony jako posrtowany
			// malejąco. Najwyższe nry startowe na początku a potem malejąco w strone jedynki
			Vector<LugerCompetitor> ex = currentCompetition.returnLugersWithThisRank(lowestRank, true);
			
			// takie przypisanie jest uniwersalne i zadziała poprawnie bez względu czy ostatnie miejsce jest z kimś
			// ex-aequo czy nie. Jeżeli nie jest to wektor zawiera jeden element, jeżeli jest to pierwszy element
			// bedzie zawierał sankarza o najwyższym numerze
			returnValue = ex.get(0);
		}
		// jeżeli nie należy stosować kolejności FIL to trzeba znaleźdź sankarza z najniższym numerem startowym i jego 
		// zwrócić z metody jako pierwszego to startu
		else {
			for (int i = 1; i <= currentCompetition.invertedStartList.size(); i++) {
				// próba wyciągnięcia sankarza o tym numerze startowym
				LugerCompetitor v = currentCompetition.invertedStartList.get((short)i);
				
				// sprawdzenie czy taki numer startowy w ogóle istnieje
				if (v != null) {
					// jeżeli istnieje to przerwij dalsze sprawdzanie i zróć właśnie tego
					returnValue = v;
					break;
				}
			}
		}
		
		return returnValue;
	}

	@Override
	public LugerCompetitor getSecond(Competition currentCompetition) {
		LugerCompetitor returnValue = null;
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"
		
		// jeżeli nalezy tu zastosować kolejność FIL to najpierw trzeba odszukać sankarza, który zajął po ostatnim ślizgu
		// najniższą lokatę a potem znaleźdź koljnego za nim
		if (applyFilOrder) {
			
			short lowestRank = 0;
			
			// określanie jaka jest ostatnia lokata
			for (Entry <LugerCompetitor, Short> e : currentCompetition.ranks.entrySet()) {
				// pomijaj w analizie wszytkie lokaty 999, czyli zawodników DNF/DSQ/DNS
				if (e.getValue().equals(new Short((short) 999))) {
					continue;
				}
				
				// jeżeli miejsce (lokata) aktualnie sprawdzanego sankarza jest niższe niż ostatnie najniższe
				// to zaktalizuj odpowiednią zmienną
				if (e.getValue() > lowestRank)
					lowestRank = e.getValue();
			}
			
			// sprawdzanie czy ostatnia lokate nie jest ex-aequo
			Vector<LugerCompetitor> ex = currentCompetition.returnLugersWithThisRank(lowestRank, true);
			
			// jeżeli ostatnie miejsce jest z kim ex.equo to trzeba zwrócić sankarza który ma kolejny mniejszy
			// nr startowy
			if (ex.size() > 1) {
				returnValue = ex.get(1);
			}
			
			// jeżeli ostatnie miejsce nie jest ex-aequo to trzeba zwórić kolejnego który miał wyższa lokatę
			else {
				// ale jednocześnie trzeba sprawdzić czy nie doszło do sytacji, w której za ostatnim miejscem idąc w stronę pierwszego
				// jest przerwa w puktacji związana z innym ex-aequo
				short rankToCheck = (short) (lowestRank - 1); // sprawdzanie zaczyna się od przedostatniego miejsca
				Vector<LugerCompetitor> n;
				
				do {
					n = currentCompetition.returnLugersWithThisRank(rankToCheck, true);
					if (n != null && n.size() > 0)
						break;	// wyjście z pętli na pierwszej znalezionej lokacie
					
					rankToCheck--;
					
				} while (rankToCheck > 0);
				
				//n = currentCompetition.returnLugersWithThisRank((short) (lowestRank - 1), true);
				
				if (rankToCheck == (lowestRank - 1))
					// jeżeli przedostatnie miejsce nie jest ex-aeqo to zwróć po prostu zawodnika który
					// uzyskał lokatę o jedną wyższą od najgorszej
					returnValue = n.get(0);
				else {
					// jeżeli przedostatnie miejsce jest ex-aeqo to należy zwrócić zawodnika z największym numerem
					// startowym ze wszystkich którzy mają tę lokatę
					returnValue = n.get(0);
				}
			}
		}
		// jeżeli nie należy stosować kolejności FIL to trzeba znaleźdź sankarza z najniższym numerem startowym i jego 
		// zwrócić z metody jako pierwszego to startu
		else {
			int firstToCheck = this.getFirst(currentCompetition).getStartNumber(); 
			
			for (int i = firstToCheck + 1; i <= currentCompetition.getLastStartNumber(); i++) {
				// próba wyciągnięcia sankarza o tym numerze startowym
				LugerCompetitor v = currentCompetition.invertedStartList.get((short)i);
				
				// sprawdzenie czy taki numer startowy w ogóle istnieje
				if (v != null) {
					// jeżeli istnieje to przerwij dalsze sprawdzanie i zróć właśnie tego
					returnValue = v;
					break;
				}
			}
		}
		
		return returnValue;	
		}

	@Override
	public boolean checkIfLastInRun(LugerCompetitor in, Competition currentCompetition, Run currentRun) {
		short startNumberToCheck = in.getStartNumber();
		boolean returnValue = true;
		LocalTime zero = LocalTime.of(0, 0, 0, 0); ///////
		
		Short lowestRank = this.lowestRankWithZeroRuntime(currentCompetition, currentRun);
		Short highestRank = this.highestRankWithZeroRuntime(currentCompetition, currentRun);
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"
		
		// jeżeli jest to ślizg/zjazd w którym należy zastosować kolejność FIL
		if (applyFilOrder) { 
			Short startnum = startNumberToCheck;
			boolean loopBreak = true;
			
			// pętla chodzi po wszystkich zawodnikach przed aktualnie sprawdzanym i sprawdza ich czas ślizgu
			do {
				try {
					startnum = this.nextStartNumber(startnum, currentCompetition);
				} catch (LugerDoesntExist e) {
					e.printStackTrace();
				}
				
				if (startnum != null) {
				
					LugerCompetitor cmptr = currentCompetition.invertedStartList.get(startnum);
					
					LocalTime timeToCheck = currentRun.totalTimes.get(cmptr);	// czas ślizgu/przejazdu do sprawdzenia
					
					if (timeToCheck.equals(zero)) {
						// jeżeli czas ślizgu/przejazdu jakiegokolwiek zawodnika przed aktualie sprawdzanm jest
						// równy zero, to oznacza to, że ten sprawdzany nie jest ostatni
						returnValue = false;
						loopBreak = false;
					}
				
				}
				else {
					loopBreak = false;
				}
			} while (loopBreak);
		}
		else {
			SimpleOrder simple = new SimpleOrder();
			
			returnValue = simple.checkIfLastInRun(in, currentCompetition, currentRun);
		}
		
		return returnValue;
	}

	@Override
	public boolean checkIfLastInRun(short startNum, Competition currentCompetition) {
		
		short startNumberToCheck = startNum;
		boolean returnValue = true;
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		LocalTime m = LocalTime.of(0, 59);
		Run currentRun = null;
		
		Short nextStartNumToCheck;	
		
		// ustawiane na true jeżeli aktualnie leci ślizg w którym należy stosować kolejność FIL
		boolean applyFilOrder = false;
		
		// spawdzanie preconitions do ewentalnego włączenia kolejności FIL
		if (currentCompetition.isCurrentRunTraining == false && currentCompetition.isCurrentRunFirstScored == false) 
			applyFilOrder = true;
		else
			applyFilOrder = false; // w ślizgach treningowych i pierwszym punktowanym obowiązuje kolejność "prosta"

		if (applyFilOrder) {
			// poszukiwanie aktualego ślizgu/zjazdu
			for (Run r : currentCompetition.runsTimes) {
				if (r.done)
					continue;
				else
					currentRun = r;
			}
			
			// określanie kolejnego numeru startowego po sprawdzanym
			try {
				nextStartNumToCheck = this.nextStartNumber(startNumberToCheck, currentCompetition);
			} catch (LugerDoesntExist e) {
				e.printStackTrace();
				nextStartNumToCheck = null;
			}
			
			// pętla chodzi po wszystkich zawodnikach przed aktualnie sprawdzanym i sprawdza ich czas ślizgu
			// jeżeli kolejny po sprawdzanym numerze nie ma żadnego kolejnego to znaczy że to koniec konkurencji
			while (nextStartNumToCheck != null) {
				LugerCompetitor in = currentCompetition.invertedStartList.get(nextStartNumToCheck);
				LocalTime timeToCheck = currentRun.totalTimes.get(in);	// czas ślizgu/przejazdu do sprawdzenia
				
				// TODO: ?
				//returnValue = false;
				
				if (timeToCheck.equals(zero)) {
					// jeżeli czas ślizgu/przejazdu jakiegokolwiek zawodnika przed aktualie sprawdzanm jest
					// równy zero, to oznacza to, że ten sprawdzany nie jest ostatni
					returnValue = false;
					break;
				}
				
				// wyciąganie kolejnego do sprawdzenia
				try {
					nextStartNumToCheck = this.nextStartNumber(nextStartNumToCheck, currentCompetition);
				} catch (LugerDoesntExist e) {
					e.printStackTrace();
				}
				
			}
		}
		else {
			SimpleOrder order = new SimpleOrder();
			
			returnValue = order.checkIfLastInRun(startNumberToCheck, currentCompetition);
		}
		
		return returnValue;
	
		
	}

	@Override
	public String toString() {
		return "FIL_ORDER";
	}

	@Override
	public Short highestRankWithZeroRuntime(Competition currentCompetition, Run currentRun) {

		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		// Przyjmuje się najniższą możliwą rangę i idzie w górę 
		Short out = 256; 
		Set<Entry<LugerCompetitor, Short>> ranksSet = currentCompetition.ranks.entrySet();
		
		for (Entry<LugerCompetitor, Short> e : ranksSet) {
			// saneczkarz do sprawdzenia
			LugerCompetitor k = e.getKey();
			
			// wyciąganie czasu ślizgu w aktualnym ślizgu dla tego saneczkarza
			LocalTime runtime = currentRun.totalTimes.get(k);
			
			// sprawdzanie czy obecnie sprawdzany saneczkarz jechał w tym ślizgu..
			if (runtime == null || runtime.equals(zero)) {
				
				// jeżeli nie jechał to sprawdź jego lokatę
				if (e.getValue() < out) {
					// jeżeli jest niższa (bliższa 	1) niż ostatnio sprawdzan
					// to podmień ostatnio sprawdzaną
					out = e.getValue();
				}
				else {
					// jeżeli nie jest mniejsza to nic nie rób
					;
				}
			}
			
			
		}
		
		// jeżeli nie znaleziono nikogo to zwróć null co sugeruje że w aktualnie przeszukiwanym ślizgu
		// wszyscy już jechali
		if (out == 256)
			return null;
		else
			return out;
	}

	@Override
	public Short lowestRankWithZeroRuntime(Competition currentCompetition, Run currentRun) {

		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		// Przyjmuje się zero
		Short out = 0; 
		Set<Entry<LugerCompetitor, Short>> ranksSet = currentCompetition.ranks.entrySet();
		
		for (Entry<LugerCompetitor, Short> e : ranksSet) {
			// saneczkarz do sprawdzenia
			LugerCompetitor k = e.getKey();
			
			// wyciąganie czasu ślizgu w aktualnym ślizgu dla tego saneczkarza
			LocalTime runtime = currentRun.totalTimes.get(k);
			
			// sprawdzanie czy obecnie sprawdzany saneczkarz jechał w tym ślizgu..
			if (runtime == null || runtime.equals(zero)) {
				
				// jeżeli nie jechał to sprawdź jego lokatę
				if (e.getValue() > out) {
					// jeżeli jest wyższa (gorsza - dalsza od 1) niż ostatnio sprawzdzona
					// to podmień
					out = e.getValue();
				}
				else {
					// jeżeli nie jest mniejsza to nic nie rób
					;
				}
			}
			
			
		}
		
		// jeżeli nie znaleziono nikogo to zwróć null co sugeruje że w aktualnie przeszukiwanym ślizgu
		// wszyscy już jechali
		if (out == 256)
			return null;
		else
			return out;
	}

}
