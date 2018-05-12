package pl.jeleniagora.mks.events;

import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.SpringS;

/**
 * Klasa obsługująca zdarzenie obliczenia czasu ślizgu dla zawodnika.
 * @author mateusz
 *
 */
public class RuntimeUpdateEvent {

	AnnotationConfigApplicationContext ctx;
	
	public RuntimeUpdateEvent(AnnotationConfigApplicationContext context) {
		this.ctx = context;
	}
	
	/**
	 * Statyczna metoda wywoływana z klasy Chrono po obliczeniu czasu ślizgu. W niej są kolejne 
	 * odwołania do innych klas i metod, które cokolwiek robią z tymi danymi
	 * 
	 * @param runTime
	 */
	public void processingChain(LocalTime runTime) {
//		this.updateTextFieldsInCM(runTime);
	}
	

}
