package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.EndOfCompetitionEx;
import pl.jeleniagora.mks.types.LugerCompetitor;

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
	 * Klasa  
	 * @throws EndOfCompetitionEx Rzucany po ślizgu ostatniego saneczkarza, co oznacza zakończenie konkurencji
	 * @throws AppContextUninitializedEx Rzucany jeżeli nie ustawiono wczesniej kontekstu aplikacji
	 */
	public static void moveForwardNormally() throws EndOfCompetitionEx, AppContextUninitializedEx {
		if (ctx == null) 
			throw new AppContextUninitializedEx();
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");

		LugerCompetitor actual = rte_st.actuallyOnTrack;
		
		/*
		 * Numery startowe sa liczone nie po programistycznemu czyli od jedynki a nie od zera!!!
		 */
		Short actualStartNumber = rte_st.currentCompetition.startList.get(actual);
		
		/*
		 * Sprawdzanie czy nie doszło się do końca aktualnej konkurencji. Tj czy aktualny na torze nie jest
		 * ostatnim bądź przedostatnim
		 */
		if (actualStartNumber <= rte_st.currentCompetition.competitorsCount - 2)
		{
			rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(actualStartNumber + 1));
			rte_st.nextOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(actualStartNumber + 2));
		}
		else if(actualStartNumber <= rte_st.currentCompetition.competitorsCount - 1)
		{
			/*
			 * Jeżeli teraz pojechał przedostatni...
			 */
			rte_st.actuallyOnTrack = rte_st.currentCompetition.invertedStartList.get((short)(actualStartNumber + 1));
			rte_st.nextOnTrack = null;
		}
		else {
			/*
			 * Jeżeli ten był ostatni to trzeba zakończyć konkurencje
			 */
			throw new EndOfCompetitionEx();
		}
		
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
