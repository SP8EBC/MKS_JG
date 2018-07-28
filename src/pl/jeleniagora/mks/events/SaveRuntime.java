package pl.jeleniagora.mks.events;

import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.display.DisplayRuntimeAndStartNum;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.online.WebServicePutConsumer;
import pl.jeleniagora.mks.online.renderers.CompetitionDataRenderer;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

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
	
	public static void displayRuntimeOnDisplay(LocalTime runTime, LugerCompetitor whoFinished) {
		DisplayRuntimeAndStartNum d = new DisplayRuntimeAndStartNum(RTE.getRte_disp_interface());
		
		/*
		 * Przetrzymanie treści na wyświetlaczu wynosi 2500 bo po 3 sekundach nastąpi autozapis
		 */
		d.showScoreAfterRunAndDelay(runTime, whoFinished, 9000);
	}
	
	/**
	 * Metoda służy do zapisywania czasu ślizgu dla zawodnika i ślizgu klikniętego w tabeli wyników w CompManager.
	 * @param runTime
	 * 
	 */
	public static boolean saveRuntimeForMarkedCmptr(LocalTime runTime) {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		boolean returnVal = false;
		
		if (rte_gui.competitorClickedInTable == rte_st.actuallyOnTrack) {
			returnVal = true;
		}
		
		rte_gui.runClickedInTable.totalTimes.put(rte_gui.competitorClickedInTable, runTime);
		
		try {
			rte_gui.model.updateTableData(rte_gui.competitionBeingShown, false);
		} catch (UninitializedCompEx e) {

		}
		rte_gui.model.fireTableDataChanged();
		
		WebServicePutConsumer ws = new WebServicePutConsumer((AfterWebServiceContact) ctx.getBean("afterWebServiceContact"));
		ws.competitionDataUpdater(rte_gui.competitionBeingShown);
		
		return returnVal;
	}
	
	/**
	 * Metoda służy do zapisywania czasu w bierzącym ślizgu dla zawodnika aktualnie na torze, 
	 * czyli tego który właśnie dojechał do mety
	 * @param runTime
	 */
	public static void saveRuntimeForCurrentCmptr(LocalTime runTime) {

		System.out.println("saveRuntmeForCurrentCmptr");
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		rte_st.currentRun.totalTimes.put(rte_st.actuallyOnTrack, runTime);
		
		try {
			rte_gui.model.updateTableData(rte_gui.competitionBeingShown, false);
		} catch (UninitializedCompEx e) {

		}
		rte_gui.model.fireTableDataChanged();
		
		WebServicePutConsumer ws = new WebServicePutConsumer((AfterWebServiceContact) ctx.getBean("afterWebServiceContact"));
		ws.competitionDataUpdater(rte_st.currentCompetition);
		
	}
}
