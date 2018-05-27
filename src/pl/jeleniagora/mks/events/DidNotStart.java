package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.DNS;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa zapisuje "nie wystartował" saneczkarzowi. 
 * @author mateusz
 *
 */
public class DidNotStart {
	
	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void process() {
		setNotStartForCurrentLuger();
	}
	
	static void setNotStartForCurrentLuger() {

		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		LugerCompetitor l = rte_st.actuallyOnTrack;
		
		if (rte_st.currentRun.trainingOrScored) {
			/*
			 * W ślizbu puntkowanym na zawodach nie wystartowanie dyskwalifikuje z dalszej cześci zawodów
			 */
			for (Run r: rte_st.currentCompetition.runsTimes) {
				r.run.put(l, DNS.getValue());
			}
		}
		else {
			/*
			 * Podczas treningu nie wystartowanie w jednym ślizgu w dalszym ciągu umożliwia następne zjazdy/ślizgi
			 */
			rte_st.currentRun.run.put(l, DNS.getValue());

		}
	}
}
