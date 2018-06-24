package pl.jeleniagora.mks.main;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.exceptions.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.CommThread;
import pl.jeleniagora.mks.serial.CommThreadTermHook;
import pl.jeleniagora.mks.serial.RxCommType;
import pl.jeleniagora.mks.settings.SerialCommS;
import pl.jeleniagora.mks.settings.SpringS;

/**
 * Główna klasa stanowiąca punkt wejścia do programu 
 * @author mateusz
 *
 */
public class Main {

	private static CommThread com;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringS.class);
			
			RTE_COM rte_com = ctx.getBean(RTE_COM.class);
			
			com = new CommThread("/dev/ttyUSB0", rte_com, true);
			System.out.println("done");
			
			//Chrono chrono = new Chrono(ctx);
			new Thread(new Chrono(ctx)).start();
	
			Runtime.getRuntime().addShutdownHook(new Thread(new CommThreadTermHook(ctx)));
			
			SerialCommS.setMaxRxTimeoutMsec(2000);
			rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
			rte_com.numberOfBytesToRx = 14;
			rte_com.activateRx = true;
			
			com.startThreads();
			
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FailedOpenSerialPortEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
