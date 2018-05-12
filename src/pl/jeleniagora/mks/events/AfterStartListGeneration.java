package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;

/**
 * Klasa obsługująca zdarzenie po wygenerowaniu listy startowej - numerów startowych. Inicjalizuje cały system
 * obsługi zawodow / treningów ustawiając wartości początkowe. Ustawia pierwszą konkurencję jako aktualną, 
 * ustawia aktualny ślizg, saneczkarza itp.
 */
public class AfterStartListGeneration {
	
	static AnnotationConfigApplicationContext ctx;
	
	private AfterStartListGeneration(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}

	public static void process(Competitions competitions) {
		
		AfterStartListGeneration.setCurrentCompetition(competitions);
		AfterStartListGeneration.setCurrentAndNextLuger(competitions);

	}
	
	static void setCurrentCompetition(Competitions competitions) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		if (rte_st.competitionsDone == null || rte_st.competitionsDone.isEmpty()) {
			/*
			 * Jeżeli wektora zakończonych konkurencji w ogóle nie ma albo jest pusty to oznacza to,
			 * że jeszcze żadna nie została zakończona. W związku z tym jako aktualnie rozgrywana konkurencja
			 * zostanie ustawiona ta pierwsza (przechowywana z indeksem zero 
			 */
			rte_st.currentCompetition = competitions.competitions.firstElement();
			
			/*
			 * Ustawianie aktualnego ślizgu jako pierwszego ślizgu w pierwszej konkurencji
			 */
			rte_st.currentRun = competitions.competitions.firstElement().runsTimes.get(0);

		}
	}
	
	static void setCurrentAndNextLuger(Competitions competitions) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");

		rte_st.actuallyOnTrack = competitions.competitions.firstElement().invertedStartList.get((short)1);
		rte_st.nextOnTrack = competitions.competitions.firstElement().invertedStartList.get((short)2);
		
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
		

	}
	
}
