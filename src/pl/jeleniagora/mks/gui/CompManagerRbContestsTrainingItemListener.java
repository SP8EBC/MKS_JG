package pl.jeleniagora.mks.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;

public class CompManagerRbContestsTrainingItemListener implements ItemListener {

	AnnotationConfigApplicationContext ctx;
	
	public CompManagerRbContestsTrainingItemListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		if (arg0.getStateChange() == ItemEvent.SELECTED) {
			rte_st.currentCompetition.trainingOrContest = false;
			JOptionPane.showMessageDialog(null, "Konfiguracja zawodów przełączona na tryb treningowy, punktacja będzie liczona po każdej konkurencji");
		}
		else if (arg0.getStateChange() == ItemEvent.DESELECTED) {
			rte_st.currentCompetition.trainingOrContest = true;
			JOptionPane.showMessageDialog(null, "Konfiguracja zawodów przełączona na tryb 'zawody', punktacja będzie liczona tylko dla ślizgów punktowanych");

		}
		else;
	}


}
