package pl.jeleniagora.mks.chrono;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.TypesConverters;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SimulateChrono {

	@Autowired
	@Qualifier("comBean")
	RTE_COM rte_com;
	
	@Autowired
	RTE_CHRONO rte_chrono;
	
	int w = 1;
	
	public SimulateChrono() {
		
	}
	
	class Simulate implements Runnable {

		@Override
		public void run() {
			Vector<Character> two_sec_1 = TypesConverters.convertStringToCharacterVector("CZI10908045a1");
			Vector<Character> two_sec_2 = TypesConverters.convertStringToCharacterVector("CZI30908065a1");
			
			Vector<Character> two_sec_200msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
			Vector<Character> two_sec_200msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a1");

			Vector<Character> two_sec_201msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
			Vector<Character> two_sec_201msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a2");
			
			Vector<Character> first = null;
			Vector<Character> second = null;
			
			switch (w) {
			case 1:
				first = two_sec_1;
				second = two_sec_2;
				break;
				
			case 2:
				first = two_sec_200msec_1;
				second = two_sec_200msec_2;
				
			case 3:
				first = two_sec_201msec_1;
				second = two_sec_201msec_2;
			}
			
			rte_com.rxBuffer = first;
			rte_com.rxDataAvaliable = true;
			
			synchronized (rte_chrono.syncState) {
				try {
					rte_chrono.syncState.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			//	act = rte_chrono.timeMeasState;
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			rte_com.rxBuffer = second;
			rte_com.rxDataAvaliable = true;	
		}
		
	}
	
	public void simulate(int what) {
		w = what;

		new Thread(new Simulate()).start();
		
	}
}
