package pl.jeleniagora.mks.events;

import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
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
		
		System.out.println("SaveRuntimeDelayThread started");
		
		/*
		 * Czekanie 5 sekund
		 */
		try {
			Thread.sleep(5000, 0);
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
			} catch (EndOfRunEx | AppContextUninitializedEx e) {
				e.printStackTrace();
			}
		}
		else {
			return;
		}
		
	}
}
