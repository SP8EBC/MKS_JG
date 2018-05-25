package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.DidNotFinished;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.UninitializedCompEx;

public class CompManagerDsqBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;
	
	CompManagerDsqBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		if (!rte_st.currentRun.trainingOrScored) {
			/*
			 * W ślizgu treningowym z definicji nie ma dyskwalifikacji. Jeżeli zawodnik z jakichś powodów
			 * nie jedzie to po prostu trzeba go ręcznie przeskoczyć 
			 */
			JOptionPane.showMessageDialog(null, "W ślizgu treningowym nie ma dyskwalifikacji!");
			return;
		}
		
		int answer = JOptionPane.YES_NO_OPTION;
		answer = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zdyskwalifikować zawodnika aktualnie na torze?", "Pozor!", answer);
		
		
		if (answer == JOptionPane.YES_OPTION) {
			
			DidNotFinished.setNotFinishedForLuger();
			
			try {
				rte_gui.compManagerScoreModel.updateTableData(rte_st.currentCompetition, false);
			} catch (UninitializedCompEx e) {

			}
			rte_gui.model.fireTableDataChanged();
		}
		else {
			
		}
		
	}

	
}
