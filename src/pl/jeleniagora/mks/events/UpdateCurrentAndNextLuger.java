package pl.jeleniagora.mks.events;

import java.time.LocalTime;
import java.util.Map.Entry;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.EndOfRunEx;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa zawierająca statyczną metode używaną do ustawiania w menadżerze zawodów i ogólnie w programie
 * saneczkarza aktualnie na torze. Metoda oczywiście ma side effects
 * @author mateusz
 *
 */
public class UpdateCurrentAndNextLuger {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Metoda używana do ustawiania kontekstu aplikacji. Musi być wywołana przed pierwszym użyciem
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	/**
	 * Prywatny konstruktor uniemożliwia stworzenie egzemplarza tej klasy -> możliwy tylko statyczny
	 * dostęp
	 */
	private UpdateCurrentAndNextLuger() {
		
	}
	
	/**
	 * Prywatna metoda służąca do wyszukiwania pierwszego zawodnika który albo nie ma w ogóle czasu
	 * przejazdu albo ma zapisane 0:00.000 - generalnie wyszukuje tego co jeszcze nie jechał
	 * @return
	 */
	public static LugerCompetitor findFirstWithoutTime() {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		Run current = rte_st.currentRun;
		
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		LugerCompetitor returnVal = null; 
		
		/*
		 * Wyciąganie entrySet i wynajdywanie pierwszego saneczkarza bez przypisanego uzyskanego
		 * czasu ślizgu
		 */
		for (Entry<LugerCompetitor, LocalTime> e : current.run.entrySet()) {
			if (e.getValue() == null || e.getValue().equals(zero)) {
				returnVal = e.getKey();
				break;
			}
		}

		
		return returnVal;
	}
	
	/**
	 * Metoda powraca do normalnej kolejności startów - wywoływana po użyciu opcji "Powróc do kolejności wg (...)"
	 */
	public static void returnToNormalOrder() {
		//rte_st;
	}
	
	/**
	 * Metoda ustawia dowolnie wskazanego saneczkarza jako następnego a następnie wyszukuje i ustawia pierwszego
	 * kolejnego bez czasu przejazdu jako "następnie". Metoda przeznaczona do użycia w zasadzie tylko przy przełączaniu
	 * konkurencji
	 * @param startNumber
	 */
	public static void setActualFromStartNumberAndNext(short startNumber) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		LocalTime zero = LocalTime.of(0, 0, 0, 0);
		
		/*
		 * Referencja jest kasowana bo ta metoda będzie używana tylko przy zmianie konkurencji kiedy 
		 */
		rte_st.returnComptr = null;
		
		/*
		 * Ustawianie aktualnie na torze
		 */
		rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)startNumber);
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		
		for (Entry<LugerCompetitor, LocalTime> e : rte_st.currentRun.run.entrySet()) {
			if (e.getValue() == null || e.getValue().equals(zero)) {
				if (e.getKey().getStartNumber() > startNumber) {
					/*
					 * W domyśle saneczkarz którego numer startowy został przekazany do metody jeszcze nie jechał
					 * dlatego trzeba odnaleźdź pierwszego za nim
					 */
					
					rte_st.nextOnTrack = rte_st.currentCompetition.invertedStartList.get((short)e.getKey().getStartNumber());
					rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
					
					break;
				}
			}
		}
	}
	
	/**
	 * Metoda pozwalająca ustrawić dowolnie wskazanego saneczkarza jako aktualnego
	 * @param startNumber
	 */
	public static void setActualFromStartNumber(short startNumber) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		rte_st.returnComptr = rte_st.actuallyOnTrack;
		
		rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)startNumber);
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
	}
	
	/**
	 * Metoda pozwalająca ustrawić dowolnie wskazanego saneczkarza jako nastepnego
	 * @param startNumber
	 */
	public static void setNextFromStartNumber(short startNumber) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		rte_st.returnComptr = rte_st.nextOnTrack;
		
		rte_st.nextOnTrack = rte_st.currentCompetition.invertedStartList.get((short)startNumber);
		rte_gui.nextOnTrack.setText(rte_st.actuallyOnTrack.toString());


	}
	
	/**
	 * Metod ustawiająca jako aktualnie na torze pierwszego zawodnika w śligu (saneczkarz o nrze startowym jeden)
	 * a jako następnego drugiego zawodnika (nr startowy dwa)
	 */
	public static void rewindToBegin() {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)1);
		rte_st.nextOnTrack = rte_st.currentCompetition.invertedStartList.get((short)2);
		
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
		
	}
	
	/**
	 * Metoda przesuwająca "aktualnie na torze" i "następnie" na kolejnego  
	 * @throws EndOfRunEx Rzucany po ślizgu ostatniego saneczkarza, co oznacza zakończenie konkurencji
	 * @throws AppContextUninitializedEx Rzucany jeżeli nie ustawiono wczesniej kontekstu aplikacji
	 */
	public static void moveForwardNormally() throws EndOfRunEx, AppContextUninitializedEx {
		if (ctx == null) 
			throw new AppContextUninitializedEx();
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");

		LugerCompetitor actual = rte_st.actuallyOnTrack;
		LugerCompetitor next = rte_st.nextOnTrack;
		
		/*
		 * Sprawdzanie na początku czy jako następny saneczkarz nie jest ustawiony null co sugeruje że to koniec
		 * konkurencji
		 */
		if (actual != null && next == null) {
			throw new EndOfRunEx();
		}
			
		
		/*
		 * Numery startowe sa liczone nie po programistycznemu czyli od jedynki a nie od zera!!!
		 */
		Short actualStartNumber = rte_st.currentCompetition.startList.get(actual);
		Short nextStartNumber = rte_st.currentCompetition.startList.get(next);
		
		Short actualRun = rte_st.currentRunCnt;
		
		/*
		 * Sprawdzanie czy nie doszło się do końca aktualnej konkurencji, tj czy następny na torze nie jest 
		 * jednocześnie ostatnim. Srawdzenie via nasatępny takie jest wymagane ze względu na funkcjonalność "ustaw aktualnie
		 * zaznaczonego jako nastepnego (...)
		 */
		if (nextStartNumber <= rte_st.currentCompetition.competitorsCount - 1)
		{
			/*
			 * Numery startowe są liczone od jedynki, dlatego jeżeli następny będzie miał nr 9 a jest 10 wszystkich
			 * to jeszcze można przypisać kogoś za nim
			 */
			rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(nextStartNumber));
			rte_st.nextOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(nextStartNumber + 1));
		}
		else if(nextStartNumber == rte_st.currentCompetition.competitorsCount)
		{
			/*
			 * Jeżeli nastepny będzie miał nr 10 a wszstkich też jest 10 to już nie ma nikogo za nim
			 */
			rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(nextStartNumber));
			rte_st.nextOnTrack = null;
		}
		else {
			/*
			 * Jeżeli ten był ostatni to trzeba zakończyć konkurencje
			 */
			//throw new EndOfRunEx();
		}
		
		/*
		 * Podświetlanie zawodnika w menadżerze zawodów
		 */
		rte_gui.compManager.markConreteRun(actualStartNumber, actualRun);
		
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		try {
			/*
			 * Jeżeli metoda została wywołana po ślizgu przedostatniego saneczkarza w konkurencji
			 * do po aktualizacji referencja na "następny na torze" zostanie ustawiona na null
			 * aby zasygnalizować że już nie ma następnego
			 */
			rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
		}
		catch (NullPointerException e) {
			/*
			 * Jeżeli nie ma następnego w konkurencji (null pointer) to wyświetl po prostu "brak"
			 */
			rte_gui.nextOnTrack.setText("----");
		}
		
	}
}
