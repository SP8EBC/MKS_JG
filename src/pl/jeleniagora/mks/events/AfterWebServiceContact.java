package pl.jeleniagora.mks.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_GUI;

/**
 * Klasa zawierająca zdarzenia wyzwalane po zakończonej komunikacji z WS
 * @author mateusz
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AfterWebServiceContact {

	@Autowired
	RTE_GUI rte_gui;
	
	/**
	 * Brak komunikacji z serwerem
	 */
	public void noCommWithServer() {
		rte_gui.lblredniaPrdko.setText("Brak komunikacji z serwerem");
	}
	
	public void errorStatusReturned(HttpStatus status) {
		
	}
}
