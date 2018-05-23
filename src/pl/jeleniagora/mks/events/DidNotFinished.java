package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.DNF;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class DidNotFinished {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void setNotFinishedForLuger() {
	
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		LugerCompetitor l = rte_st.actuallyOnTrack;
		
		if (rte_st.currentRun.trainingOrScored) {
			/*
			 * W ślizgu treningowym nie dojechanie do mety przez zawodnika dyskwalifikuje go całkowicie 
			 * z dalszej części zawodów
			 */
			for (Run r: rte_st.currentCompetition.runsTimes) {
				r.run.put(l, DNF.getValue());
			}
		}
		else {
			/*
			 * Podczas treningu nie ukończenie ślizgu nie ma wpływu na możliwość dalszych startów
			 */
			rte_st.currentRun.run.put(l, DNF.getValue());

		}
	}
}
