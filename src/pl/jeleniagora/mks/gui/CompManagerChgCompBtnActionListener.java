package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.ChangeCompetition;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.MissingCompetitionEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

public class CompManagerChgCompBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerChgCompBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {

		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		int answer = JOptionPane.YES_NO_OPTION;
		answer = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zmienić aktualną konkurencję?", "Pozor!", answer);
		
		if (answer == JOptionPane.YES_OPTION) {
			
			System.out.println("Changing current competition to: " + rte_gui.competitionBeingShown.toString()); 
			
			try {
				ChangeCompetition.changeActualCompetition(rte_gui.competitionBeingShown, true);
			} catch (AppContextUninitializedEx e) {
				e.printStackTrace();
			} catch (MissingCompetitionEx e) {
				e.printStackTrace();
			}
			
			rte_gui.currentCompetition.setText(rte_st.currentCompetition.toString());

			
			LugerCompetitor cmpr = UpdateCurrentAndNextLuger.findFirstWithoutTime();
			
			if (cmpr != null) {
				UpdateCurrentAndNextLuger.setActualFromStartNumberAndNext(cmpr.getStartNumber());
				
			}
			else {
				;
			}

			String msgString = new String();
			if (rte_st.currentRun.trainingOrScored) {
				msgString += "Punktowany ";
			}
			else {
				msgString += "Treningowy ";
			}
			msgString += rte_st.currentRun.numberInScoredOrTrainingRuns + 1;
			
			/*
			 * Podświetlanie aktualnie na torze
			 */
			Short actualRun = rte_st.currentRunCnt;
			rte_gui.compManager.markConreteRun(rte_st.actuallyOnTrack.getStartNumber(), actualRun);
			rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
			rte_gui.runClickedInTable = rte_st.currentRun;

			
			JOptionPane.showMessageDialog(null, "Konkurencja została pomyślnie zmieniona - aktualny ślizg: " + msgString);
			
			
		}
		else {
			;
		}
		
	}

}
