package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Vector;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.chrono.ChronoStateMachine;
import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.SpringS;

class RunChangeTest {

	AnnotationConfigApplicationContext ctx;

	
	@BeforeEach
	void setUp() throws Exception {
		ctx = new AnnotationConfigApplicationContext(SpringS.class);
		
		ChronometerS.gateCount = 2;
		
		LandedStateReached.setAppCtx(ctx);
		UpdateCurrentAndNextLuger.setAppCtx(ctx);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Vector<Character> two_sec_1 = TypesConverters.convertStringToCharacterVector("CZI10908045a1");
		Vector<Character> two_sec_2 = TypesConverters.convertStringToCharacterVector("CZI30908065a1");

		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		RTE_CHRONO rte_chrono = ctx.getBean(RTE_CHRONO.class);
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		
		ChronoStateMachine act;
		LocalTime tim;
		
		/*
		 * Uruchamianie menadżera zawodów
		 */
		CompManager.utMain(ctx);

		new Thread(new Chrono(ctx)).start();

		System.out.println("Waiting for the CompManager to became fully operational.. ");
		
		synchronized(rte_gui.syncCompManagerRdy) {
			try {
				rte_gui.syncCompManagerRdy.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * W tym miejscu powinien być ustawiony pierwszy ślizg i pierwszy zawodnik
		 */
		Assert.assertEquals(rte_st.currentRun, rte_st.currentCompetition.runsTimes.elementAt(0));
		Assert.assertEquals(rte_st.currentRunCnt, 0);
		
		Assert.assertEquals(rte_st.actuallyOnTrack, rte_st.currentCompetition.invertedStartList.get((short)1));
		Assert.assertEquals(rte_st.nextOnTrack, rte_st.currentCompetition.invertedStartList.get((short)2));

		
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
		
		/*
		 * W tym miejscu powinno przeskoczyć a drugiego zawodnika
		 */
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2), tim);
		
		Assert.assertEquals(rte_st.actuallyOnTrack, rte_st.currentCompetition.invertedStartList.get((short)2));
		Assert.assertEquals(rte_st.nextOnTrack, null);
		
		/*
		 * Po tej symulacji komunikacji powinno przeskoczyć na następną konkurencję
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
		
	}

}
