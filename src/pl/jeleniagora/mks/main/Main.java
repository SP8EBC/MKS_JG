package pl.jeleniagora.mks.main;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.CommThread;
import pl.jeleniagora.mks.serial.CommThreadTermHook;
import pl.jeleniagora.mks.serial.RxCommType;
import pl.jeleniagora.mks.settings.SerialCommS;
import pl.jeleniagora.mks.settings.SpringS;
import pl.jeleniagora.mks.types.FailedOpenSerialPortEx;

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
			
			com = new CommThread("/dev/ttyS0", ctx);
			System.out.println("done");
			
			//Chrono chrono = new Chrono(ctx);
			new Thread(new Chrono(ctx)).start();
	
			Runtime.getRuntime().addShutdownHook(new Thread(new CommThreadTermHook(ctx)));
			
			rte_com.activateRx = true;
			rte_com.rxCommType = RxCommType.END_OF_LINE;
			
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
