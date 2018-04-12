package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Vector;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.chrono.ChronoStateMachine;
import pl.jeleniagora.mks.chrono.RuntimeUpdateEvent;
import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.SpringS;

class RuntimeUpdateEventTest {

	@Test
	void testProcessingChain() {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringS.class);

		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		RTE_CHRONO rte_chrono = ctx.getBean(RTE_CHRONO.class);
		
		ChronoStateMachine act;
		LocalTime tim;
		
		Vector<Character> two_sec_1 = TypesConverters.convertStringToCharacterVector("CZIa0908045a1");
		Vector<Character> two_sec_2 = TypesConverters.convertStringToCharacterVector("CZIa0908065a1");
		
		/*
		 * Uruchamianie menadżera zawodów
		 */
		CompManager.utMain(ctx);

		new Thread(new Chrono(ctx)).start();

		/*
		 * Symulacja komunikacji z chronometru i obliczanie czasu ślizgu
		 */
		rte_com.rxBuffer = two_sec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2), tim);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*
		 * Jeżeli do tego miejsca test jednostkowy się nie wywalił to znaczy, że klasa Chrono działa dobrze i można
		 * spróbować te dane testowe wciepać do klasy RuntimeUpdateEvent
		 * 
		 * Pozor! Docelowo RuntimeUpdateEvent będzie uruchamiana przez Chrono w domyśle.
		 *
		 */
		
		RuntimeUpdateEvent updateEv = new RuntimeUpdateEvent(ctx);
		updateEv.processingChain(tim);
		
		/*
		 * Sleep żeby można było optycznie sprawdzić czy pola się zaktualizowały
		 */
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
