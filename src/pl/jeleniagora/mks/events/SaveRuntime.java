package pl.jeleniagora.mks.events;

import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.UninitializedCompEx;

public class SaveRuntime {


	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	private SaveRuntime() {
		
	}
	
	/**
	 * Metoda służy do zapisywania czasu ślizgu dla zawodnika i ślizgu klikniętego w tabeli wyników w CompManager.
	 * @param runTime
	 */
	public static void saveRuntimeForMarkedCmptr(LocalTime runTime) {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		rte_st.currentRun.run.put(rte_gui.competitorClickedInTable, runTime);
		
		try {
			rte_gui.model.updateTableData(rte_gui.competitionBeingShown, false);
		} catch (UninitializedCompEx e) {

		}
		rte_gui.model.fireTableDataChanged();
	}
}
