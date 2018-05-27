package pl.jeleniagora.mks.events;

import java.time.LocalTime;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.EndOfCompEx;
import pl.jeleniagora.mks.types.EndOfRunEx;

/**
 * Klasa służy do zapisu czasu ślizgu z poziomu dodatkowego wątku, który będzie opóźniał to o kilka sekund na potrzeby opcji 
 * "Autozapis czasu ślizgu"
 * 
 * 
 * @author mateusz
 *
 */
public class SaveRuntimeDelayThread implements Runnable {

	private AnnotationConfigApplicationContext ctx;
	private LocalTime rt;
	
	/**
	 * Konstruktor ustawiający 
	 * @param context
	 */
	public SaveRuntimeDelayThread(AnnotationConfigApplicationContext context, LocalTime runtime) {
		ctx = context;
		rt = runtime;
	}
	
	/**
	 * Punkt wejścia do wątku
	 */
	public void run() {
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		System.out.println("SaveRuntimeDelayThread started");
		
		boolean compHasToBeChanged = false;
		
		/*
		 * Czekanie 5 sekund
		 */
		try {
			Thread.sleep(3000, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Sprawdzanie czy użytkownik nie kliknął przez przypadek w jakiejś innej komórce w tabeli
		 */
		if (rte_gui.runtimeFromChrono) {
			SaveRuntime.saveRuntimeForCurrentCmptr(rt);
			
			try {
				UpdateCurrentAndNextLuger.moveForwardNormally();
			} catch (EndOfRunEx e) {
				
				JOptionPane.showMessageDialog(null, "Zakończył się " + rte_st.currentRun.toString());

				try {
					EndOfRun.process();
				} catch (EndOfCompEx e1) {
					compHasToBeChanged = true;
				}
				
			} catch (AppContextUninitializedEx e) {
				e.printStackTrace();
			}
		}
		else {
			return;
		}
		
	}
}
