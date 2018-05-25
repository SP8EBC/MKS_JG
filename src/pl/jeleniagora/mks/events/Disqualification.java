package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.DSQ;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa służąca do dyskwalifikacji zawodnika. Z definicji używana tylko w odniesieniu do ślizgów punktowanych bo w treningu
 * generalnie nie ma dyskwalifikacji.
 * @author mateusz
 *
 */
public class Disqualification {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void disqCurrentLuger() {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		LugerCompetitor l = rte_st.actuallyOnTrack;
		
		if (rte_st.currentRun.trainingOrScored) {

			for (Run r: rte_st.currentCompetition.runsTimes) {
				r.run.put(l, DSQ.getValue());
			}
		}
		else {
			;
		}
	}
}
