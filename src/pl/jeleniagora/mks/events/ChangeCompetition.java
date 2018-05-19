package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.MissingCompetitionEx;
import pl.jeleniagora.mks.types.Run;

public class ChangeCompetition {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	/**
	 * 
	 * @param toThis
	 * @param updateGui Ustawienie na true powoduje że metoda zmieni również konkurencję pokazywaną w CompManagerze
	 * @throws AppContextUninitializedEx 
	 * @throws MissingCompetitionEx 
	 */
	public static void changeActualCompetition(Competition toThis, boolean updateGui) throws AppContextUninitializedEx, MissingCompetitionEx {
		
		if (ctx == null) {
			throw new AppContextUninitializedEx();
		}
		
		boolean exist = false;
		Run firstNotCompleted = null;
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		/*
		 * Sprawdzenie czy podana na wejściu konkrencja istnieje w ogóle w tych zawodach
		 */
		for (Competition c : rte_st.competitions.competitions) {
			if (c.equals(toThis)) {
				exist = true;
				break;
			}
		}
		
		if (!exist)
			throw new MissingCompetitionEx();
		
		/*
		 * Przestawianie aktualnej konkurencji 
		 */
		rte_st.currentCompetition = toThis;
		
		/*
		 * Wyszukiwanie pierwszego nie zakończonego ślizgu
		 */
		
		for (Run r : rte_st.currentCompetition.runsTimes) {
			if (r.done) 
				continue;
			else {
				firstNotCompleted = r;
				break;
			}
		}
		
		if (firstNotCompleted != null) {
			rte_st.currentRun = firstNotCompleted;
			rte_st.currentRunCnt = firstNotCompleted.number;
		}
		else {
			/*
			 * Jeżeli użytkownik chce się przełączyć na konkurencję która jest już zakończona
			 */
		}
		
		/*
		 * Wyszukiwanie pierwszego saneczkarza który jeszcze nie jechał i ustawianie go jako następnego
		 */
//		LugerCompetitor current = UpdateCurrentAndNextLuger.findFirstWithoutTime();
//		UpdateCurrentAndNextLuger.setNextFromStartNumber(current.getStartNumber());
		
		
	}
	
}
