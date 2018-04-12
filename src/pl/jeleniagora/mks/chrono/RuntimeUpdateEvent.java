package pl.jeleniagora.mks.chrono;

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
		this.updateTextFieldsInCM(runTime);
	}
	
	public void updateTextFieldsInCM(LocalTime runTime) {
		
		System.out.println("RuntimeUpdateEvent");
		
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		Integer min, sec, msec;
		
		min = runTime.getMinute();
		sec = runTime.getSecond();
		msec = runTime.getNano() / TypesConverters.nanoToMilisecScaling;
		
		rte_gui.min.setText(min.toString());
		rte_gui.sec.setText(sec.toString());
		rte_gui.msec.setText(msec.toString());
	}
}
