package pl.jeleniagora.mks.events;

import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa obsługująca zdarzenie po wygenerowaniu listy startowej - numerów startowych
 */
public class AfterStartListGeneration {
	
	AnnotationConfigApplicationContext ctx;
	
	public AfterStartListGeneration(AnnotationConfigApplicationContext context) {
		ctx = context;
	}

	public void process(Competitions competitions) {
		
		this.setCurrentCompetition(competitions);
		this.setCurrentAndNextLuger(competitions);

	}
	
	void setCurrentCompetition(Competitions competitions) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		if (rte_st.competitionsDone == null || rte_st.competitionsDone.isEmpty()) {
			/*
			 * Jeżeli wektora zakończonych konkurencji w ogóle nie ma albo jest pusty to oznacza to,
			 * że jeszcze żadna nie została zakończona. W związku z tym jako aktualnie rozgrywana konkurencja
			 * zostanie ustawiona ta pierwsza (przechowywana z indeksem zero 
			 */
			rte_st.currentCompetition = competitions.competitions.firstElement();

		}
	}
	
	void setCurrentAndNextLuger(Competitions competitions) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		rte_st.actuallyOnTrack = competitions.competitions.firstElement().invertedStartList.get((short)1);
		rte_st.nextOnTrack = competitions.competitions.firstElement().invertedStartList.get((short)2);

	}
	
}
