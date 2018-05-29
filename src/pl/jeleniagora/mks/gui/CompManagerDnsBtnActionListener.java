package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.DidNotFinished;
import pl.jeleniagora.mks.events.DidNotStart;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;

public class CompManagerDnsBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;
	
	CompManagerDnsBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		int answer = JOptionPane.YES_NO_OPTION;
		answer = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz zapisać DNS dla zawodnika aktualnie na torze?", "Pozor!", answer);
		
		if (answer == JOptionPane.YES_OPTION) {
			DidNotStart.process();
			
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
