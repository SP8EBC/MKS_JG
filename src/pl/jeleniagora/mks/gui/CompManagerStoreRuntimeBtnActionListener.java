package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.SaveRuntime;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;

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
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
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
		
		boolean ret = SaveRuntime.saveRuntimeForMarkedCmptr(runTime);
		
		if (ret) {
			try {
				UpdateCurrentAndNextLuger.moveForwardNormally();
			} catch (EndOfRunEx e) {
				/*
				 * Jeżeli zapisano czas ślizgu dla ostatniego w konkurencji 
				 */
				JOptionPane.showMessageDialog(null, "Zakończył się " + rte_st.currentRun.toString());
			} catch (AppContextUninitializedEx e) {
				e.printStackTrace();
			}
		}
		else {
			;
		}

	}

}
