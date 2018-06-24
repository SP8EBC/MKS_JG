package pl.jeleniagora.mks.rte;

import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;

import pl.jeleniagora.mks.serial.RxCommType;

/**
 * RTE_COM_DISP jest to w zasadzie ta sama klasa co RTE_COM ale służy do koumunikacji przez port szeregowy
 * dla wyświetlacza
 * @author mateusz
 *
 */

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_COM_DISP extends RTE_COM {

	public RTE_COM_DISP() {
		this.waitAfterTx = 0;
	}
	
}
