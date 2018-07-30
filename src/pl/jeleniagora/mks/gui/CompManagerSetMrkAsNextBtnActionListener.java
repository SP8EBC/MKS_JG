package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;

public class CompManagerSetMrkAsNextBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerSetMrkAsNextBtnActionListener(AnnotationConfigApplicationContext ctx) {
		super();	// ???
		this.ctx = ctx;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		if (!rte_gui.shiftPressed) {
			UpdateCurrentAndNextLuger.setNextFromStartNumber(rte_gui.competitorClickedInTable.getStartNumber());
			rte_st.returnToActual = false;
			rte_gui.shiftPressed = false;
			rte_gui.btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " następnego</p></html>");
		}
		else if (rte_gui.shiftPressed) {
			UpdateCurrentAndNextLuger.setActualFromStartNumber(rte_gui.competitorClickedInTable.getStartNumber());
			rte_st.returnToActual = true;
			rte_gui.shiftPressed = false;
			rte_gui.btnWybierzZaznaczonegoW.setText("<html><p align=\"center\">Ustaw zaznaczonego " + "<br>" + " w tabeli saneczkarza jako " + "<br>" + " następnego</p></html>");
		}		
	}
	
	

}
