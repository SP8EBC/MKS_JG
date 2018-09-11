package pl.jeleniagora.mks.events;

import java.util.HashMap;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.LugerCompetitor;

@Component
public class ChangeRunManually {

	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	RTE_GUI rte_gui;
	
	public void forward() throws EndOfCompEx {
		int numberOfRuns = rte_st.currentCompetition.numberOfAllRuns;
		
		rte_st.currentRun.done = true;
		
		if (rte_st.currentRunCnt < numberOfRuns) {
			/*
			 * Jeżeli w tej konkurencji jest jeszcze jakiś ślizg
			 */
			
			rte_st.currentCompetition.partialRanks = new HashMap<LugerCompetitor, Short>();
			
			rte_st.currentRunCnt++;
			
			rte_st.currentRun = rte_st.currentCompetition.runsTimes.elementAt(rte_st.currentRunCnt);
			
			// sprawdzanie czy aktualny ślizg jest treningiem czy nie
			if (rte_st.currentRun.trainingOrScored)
				rte_st.currentCompetition.isCurrentRunTraining = false;
			else
				rte_st.currentCompetition.isCurrentRunTraining = true;
			
			// sprawdzenie czy aktualny ślizg jest pierwszym punktownm czy kolejnym punktowanym
			if (rte_st.currentRun.numberInScoredOrTrainingRuns > 0 && rte_st.currentRun.trainingOrScored) {
				rte_st.currentCompetition.isCurrentRunFirstScored = false;
			}
			else {
				rte_st.currentCompetition.isCurrentRunFirstScored = true;
			}
			
			/*
			 * ustawia też: runClickedInTable oraz competitorClickedInTable
			 */
			UpdateCurrentAndNextLuger.rewindToBegin();
			/*
			 * 
			 */
			rte_gui.compManagerScoreModel.fireTableStructureChanged();
			rte_gui.compManagerScoreModel.fireTableDataChanged();
			rte_gui.compManager.sortByStartNumberAscending();
			
			/*
			 * Czasami po zastosowaniu autosortowania tabeli po zakończonym ślizgu "odklikiwał się"
			 * pierwszy zawodnik i przeskakiwał na ostatniego wyświetlanego w tabeli. Poniższe
			 * przypisanie jest bufixem na tą przypadłość. 
			 */
			rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
			

		}
		else {
			/*
			 * Jeżeli nie to znaczy że to koniec konkurencji
			 */
			rte_st.returnComptr = null;
			JOptionPane.showMessageDialog(null, "Konkurencja zakończyła się");
			throw new EndOfCompEx();
		}
	}
	
	public void reverse() {
		int numberOfRuns = rte_st.currentCompetition.numberOfAllRuns;
		
		rte_st.currentRun.done = false;
		
		if (rte_st.currentRunCnt > 0) {
			/*
			 * Jeżeli w tej konkurencji jest jeszcze jakiś ślizg
			 */
			
			rte_st.currentCompetition.partialRanks = new HashMap<LugerCompetitor, Short>();
			
			rte_st.currentRunCnt--;
			
			rte_st.currentRun = rte_st.currentCompetition.runsTimes.elementAt(rte_st.currentRunCnt);
			
			// sprawdzanie czy aktualny ślizg jest treningiem czy nie
			if (rte_st.currentRun.trainingOrScored)
				rte_st.currentCompetition.isCurrentRunTraining = false;
			else
				rte_st.currentCompetition.isCurrentRunTraining = true;
			
			// sprawdzenie czy aktualny ślizg jest pierwszym punktownm czy kolejnym punktowanym
			if (rte_st.currentRun.numberInScoredOrTrainingRuns > 0 && rte_st.currentRun.trainingOrScored) {
				rte_st.currentCompetition.isCurrentRunFirstScored = false;
			}
			else {
				rte_st.currentCompetition.isCurrentRunFirstScored = true;
			}
			
			/*
			 * ustawia też: runClickedInTable oraz competitorClickedInTable
			 */
			UpdateCurrentAndNextLuger.rewindToBegin();
			/*
			 * 
			 */
			rte_gui.compManagerScoreModel.fireTableStructureChanged();
			rte_gui.compManagerScoreModel.fireTableDataChanged();
			rte_gui.compManager.sortByStartNumberAscending();
			
			/*
			 * Czasami po zastosowaniu autosortowania tabeli po zakończonym ślizgu "odklikiwał się"
			 * pierwszy zawodnik i przeskakiwał na ostatniego wyświetlanego w tabeli. Poniższe
			 * przypisanie jest bufixem na tą przypadłość. 
			 */
			rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
			

		}
		else {
			/*
			 * Jeżeli nie to znaczy że to koniec konkurencji
			 */
			//JOptionPane.showMessageDialog(false, "Aktualnie jesteś już na pierwszym ślizgu w konkurencji");
		}
	}
}
