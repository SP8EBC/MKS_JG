package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.rte.RTE_GUI;

public class CompManagerSetMrkAsNextBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerSetMrkAsNextBtnActionListener(AnnotationConfigApplicationContext ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		UpdateCurrentAndNextLuger.setNextFromStartNumber(rte_gui.competitorClickedInTable.getStartNumber());
	}
	
	

}
