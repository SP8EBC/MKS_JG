package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.Vector;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.chrono.ChronoStateMachine;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.SpringS;

class ChronoTest {

	@Test
	void test() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringS.class);

		ChronoStateMachine act;
		LocalTime tim;
		
		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		RTE_CHRONO rte_chrono = ctx.getBean(RTE_CHRONO.class);
		
		Vector<Character> two_sec_1 = TypesConverters.convertStringToCharacterVector("CZI10908045a1");
		Vector<Character> two_sec_2 = TypesConverters.convertStringToCharacterVector("CZI30908065a1");
		
		Vector<Character> two_sec_200msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
		Vector<Character> two_sec_200msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a1");

		Vector<Character> two_sec_201msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
		Vector<Character> two_sec_201msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a2");
		
		Vector<Character> corrupted1 = TypesConverters.convertStringToCharacterVector("CAIa0908045a1");

		
		
		new Thread(new Chrono(ctx)).start();
		
		///////////// pierwszy stan
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
		
		/////////// drugi stan
		rte_com.rxBuffer = two_sec_200msec_1;
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
		
		rte_com.rxBuffer = two_sec_200msec_2;
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
				rte_chrono.syncRuntime.wait(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2, 200*TypesConverters.nanoToMilisecScaling), tim);
		
		//////////// trzeci stan
		rte_com.rxBuffer = two_sec_201msec_1;
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
		
		rte_com.rxBuffer = corrupted1;	// wstrzykiwanie uszkodzonych danych, które nie powinny zmienic stanu
		rte_com.rxDataAvaliable = true;	
		
		synchronized(rte_chrono.syncError) {
			try {
				rte_chrono.syncError.wait(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Assert.assertEquals(true, rte_chrono.failToParseData);
		
		
		rte_com.rxBuffer = two_sec_201msec_2;
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
				rte_chrono.syncRuntime.wait(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2, 201*TypesConverters.nanoToMilisecScaling), tim);
		
	}

}
