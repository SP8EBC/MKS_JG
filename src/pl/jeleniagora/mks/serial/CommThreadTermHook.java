package pl.jeleniagora.mks.serial;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_COM;

/**
 * Wątek uruchamiany przez maszynę wirtualną Java podczas zamykania aplikacji w ten czy inny sposób.
 * Tutaj chodzi o zamknięcie portu szeregowego do chronometru. 
 * 
 * UWAGA! Przy ordynarnym zarzynaniu wątku, np przez polecenie systemowe 'kill -9' w linuksie, albo 
 * przy używaniu funkcji Stop w debuggerze to nie zostanie wykonane i port może wisieć ale najczęściej
 * przy ponownym uruchomieniu RXTX usunie martwy lockfile 
 * 
 * @author mateusz
 *
 */
public class CommThreadTermHook implements Runnable {
	
	private AnnotationConfigApplicationContext ctx;
	
	public CommThreadTermHook(AnnotationConfigApplicationContext context) {
		this.ctx = context;
	}
	
	/**
	 * 
	 */
	@Override
	public void run() {
		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		if (rte_com.isPortOpen) {
			rte_com.port.closePort();
			System.out.println("exiting..");
		}
	}

}
