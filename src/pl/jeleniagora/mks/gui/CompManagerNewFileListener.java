package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.display.DisplayStartScreen;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.Run;

/**
 * Listener od zdarzenia wyboru opcji Plik -> Nowy plik zawodów z górnego menu
 * @author mateusz
 *
 */
@Component
public class CompManagerNewFileListener implements ActionListener {

	RTE_ST rte_st;
	
	RTE_GUI rte_gui;
	
	@Autowired
	CompManagerCSelectorUpdater selectorUpdater;
	
	@Autowired
	void setGUI (RTE_GUI gui) {
		rte_gui = gui;
	}
	
	@Autowired
	void setST (RTE_ST st) {
		rte_st = st;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		int answer = JOptionPane.YES_NO_OPTION;
		answer = JOptionPane.showConfirmDialog(null, "<html><p align=\"center\">Użycie opcji spowoduje wyzerowanie programu, wszystkich wprowadzonych konkurencji,<br> czasów ślizgów itp. Czy na pewno chcesz kontynuować?</p></html>", "Pozor!", answer);

		if (answer == JOptionPane.YES_OPTION) {
			Vector<Competition> empty = new Vector<Competition>();
			
			for (Competition c : rte_st.competitions.competitions) {
				c.runsTimes.clear();
				c.startList.clear();
				c.invertedStartList.clear();
			}
			
			rte_st.competitions.competitions.clear();
			
			rte_st.competitions.listOfAllCompetingLugersInThisComps.clear();
			rte_st.competitions.listOfAllLugersInThisCompetitions.clear();
			
			rte_st.competitions = new Competitions();
			rte_st.currentRun = null;
			rte_st.actuallyOnTrack = null;
			rte_st.nextOnTrack = null;
			selectorUpdater.updateSelectorContent(empty);
			
			rte_gui.competitionBeingShown = null;
			rte_gui.competitorClickedInTable = null;
			rte_gui.competitorPreviouslyClicked = null;
			
			rte_gui.model.eraseEverything();
			rte_gui.model.fireTableStructureChanged();
			rte_gui.model.fireTableDataChanged();
			rte_gui.nextOnTrack.setText("----");
			rte_gui.actuallyOnTrack.setText("----");
			rte_gui.currentCompetition.setText("----");

			DisplayStartScreen start = new DisplayStartScreen(RTE.getRte_disp_interface());
			start.showStartScreen();
			
		}
		else;
	}

}
