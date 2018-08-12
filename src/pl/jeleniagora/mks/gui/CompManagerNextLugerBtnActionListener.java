package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.EndOfRun;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa obsługująca zdarzenia od naciśnięcia przycisku "Omiń aktualnego i przejdź (...)"
 * @author mateusz
 *
 */
public class CompManagerNextLugerBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerNextLugerBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		if (rte_st.competitions.competitions.size() == 0 ||
			rte_st.currentCompetition == null || 
			rte_st.currentCompetition.invertedStartList.size() == 0 || 
			rte_st.currentRun == null)
			return;
			
		try {
			UpdateCurrentAndNextLuger.moveForwardNormally();
		} catch (EndOfRunEx e) {

			/*
			 * Jeżeli używając tego przycisku dojechało się do końca konkurencji to trzeba zawinąć na pierwszego zawodnika bez czasu
			 */
			LugerCompetitor cmptr = null;
			try {
				cmptr = UpdateCurrentAndNextLuger.findFirstWithoutTime();
			} catch (StartOrderNotChoosenEx e1) {
				e1.printStackTrace();
			}
			try {
				UpdateCurrentAndNextLuger.setActualFromStartNumberAndNext(cmptr.getStartNumber());
			} catch (StartOrderNotChoosenEx e1) {
				e1.printStackTrace();
			}

		} catch (AppContextUninitializedEx e) {
			e.printStackTrace();
		} catch (StartOrderNotChoosenEx e) {
			e.printStackTrace();
		}
	}

}
