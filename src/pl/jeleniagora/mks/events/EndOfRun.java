package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.EndOfCompEx;

/**
 * Klasa zawierąca metody wywoływane po ostatim saneczkarzu w ślizgu
 * @author mateusz
 *
 */
public class EndOfRun {

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
	 * Prywatny konstruktor który ma uniemożliwić stworzenie egzemplarza tej klasy
	 */
	private EndOfRun() {
		
	}
	
	public static void process() throws EndOfCompEx {
		switchToNextRun();
	}
	
	static void switchToNextRun() throws EndOfCompEx {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		int numberOfRuns = rte_st.currentCompetition.numberOfAllRuns;
		
		rte_st.currentRun.done = true;
		
		if (rte_st.currentRunCnt < numberOfRuns) {
			/*
			 * Jeżeli w tej konkurencji jest jeszcze jakiś ślizg
			 */
			
			rte_st.currentRunCnt++;
			
			rte_st.currentRun = rte_st.currentCompetition.runsTimes.elementAt(rte_st.currentRunCnt);
			
			/*
			 * 
			 */
			UpdateCurrentAndNextLuger.rewindToBegin();
			/*
			 * 
			 */
			rte_gui.compManagerCSelector.setSelectedIndex(rte_st.currentRunCnt);
			rte_gui.compManagerScoreModel.fireTableStructureChanged();
			

		}
		else {
			/*
			 * Jeżeli nie to znaczy że to koniec konkurencji
			 */
			rte_st.returnComptr = null;
			throw new EndOfCompEx();
		}
		
		
	}
}
