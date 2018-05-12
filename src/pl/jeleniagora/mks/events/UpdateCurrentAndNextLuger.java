package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.EndOfCompetitionEx;
import pl.jeleniagora.mks.types.LugerCompetitor;

public class UpdateCurrentAndNextLuger {

	static AnnotationConfigApplicationContext ctx;

	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	private UpdateCurrentAndNextLuger() {
		
	}
	
	public void moveForwardNormally() throws EndOfCompetitionEx, AppContextUninitializedEx {
		if (ctx == null) 
			throw new AppContextUninitializedEx();
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

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
	}
}
