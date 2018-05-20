package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.SaveRuntime;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.UninitializedCompEx;

public class CompManagerStoreRuntimeBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerStoreRuntimeBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		Integer minutes = 0, seconds = 0, milis = 0;
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		try {
			minutes = new Integer(rte_gui.min.getText());
		}
		catch (Exception e) {
			;
		}
		
		try {
			seconds = new Integer(rte_gui.sec.getText());
		}
		catch (Exception e) {
			
		}
		
		try {
			milis = new Integer(rte_gui.msec.getText());
		}
		catch (Exception e) {
			;
		}
		
		Integer nanos = milis * CompManagerScoreTableTimeRenderer.nanoToMilisecScaling;
		
		LocalTime runTime = LocalTime.of(0, minutes, seconds, nanos);
		
		SaveRuntime.saveRuntimeForMarkedCmptr(runTime);
		

	}

}
