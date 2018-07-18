package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;
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

	public static void process(Competition competition) {
		
		AfterStartListGeneration.setCurrentCompetition(competition);
		AfterStartListGeneration.setCurrentAndNextLuger(competition);

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
			
			rte_st.currentRunCnt = 0;

		}
	}
	
	static void setCurrentCompetition(Competition competition) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		if (rte_st.competitionsDone == null || rte_st.competitionsDone.isEmpty()) {
			/*
			 * Jeżeli wektora zakończonych konkurencji w ogóle nie ma albo jest pusty to oznacza to,
			 * że jeszcze żadna nie została zakończona. W związku z tym jako aktualnie rozgrywana konkurencja
			 * zostanie ustawiona ta pierwsza (przechowywana z indeksem zero 
			 */
			rte_st.currentCompetition = competition;
			
			/*
			 * Ustawianie aktualnego ślizgu jako pierwszego ślizgu w pierwszej konkurencji
			 */
			rte_st.currentRun = competition.runsTimes.get(0);
			
			rte_st.currentRunCnt = 0;

		}
	}
	
	static void setCurrentAndNextLuger(Competitions competitions) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		Competition firstCompetition = competitions.competitions.firstElement();

		rte_st.actuallyOnTrack = firstCompetition.startOrder.getFirst(firstCompetition);
		rte_st.nextOnTrack = firstCompetition.startOrder.getSecond(firstCompetition);
		
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
		

	}
	
	static void setCurrentAndNextLuger(Competition competition) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");

		rte_st.actuallyOnTrack = competition.startOrder.getFirst(competition);
		rte_st.nextOnTrack = competition.startOrder.getSecond(competition);
		
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
		

	}
	
}
