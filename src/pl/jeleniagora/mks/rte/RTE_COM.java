package pl.jeleniagora.mks.rte;

import java.util.Vector;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;

import pl.jeleniagora.mks.serial.RxCommType;

/**
 * 
 * @author mateusz
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_COM {
	public SerialPort port;
	public Boolean isPortOpen;
	
	/**
	 * Ustawiane na true przez użytkownika aby rozpocząć odbiór danych z portu szeregowego. Kasowane przez CommThread
	 * kiedy odbiór się zakończył
	 */
	public boolean activateRx = false;
	
	/**
	 * Ustawienie na true przez użytkownika powoduje natychiastowe zakończenie odbioru i reset maszyny stanu.
	 */
	public boolean stopRx = false;
	public RxCommType rxCommType;
	
	/**
	 * Wektor rxBuffer przechowywuje dane odebrane przez port szeregowy. Dane są tam kopiowane dopiero
	 * po zakończeniu odbioru i w zależności od wyboru w rxCommType mogą być konwertowane albo na znaki
	 * (Vector<Character>) albo na typ Short aby umożliwić bezproblemowe operowanie na danych binarnych
	 * w pełnym zakresie 0-255 również przy użyciu liczb dziesiętnych. 
	 * 
	 *  Ponieważ jest to "generic type" to dalsze funkcję używające rxBuffer do swoich celów muszą wiedzieć
	 *  jak był ustawiony rxCommType aby właściwie rzutować ten wektor. Ponieważ ustawienie rxCommType może być
	 *  z jakichś względów zmieniane pomiędzy transferami jego wartość jest kopiowana to rxCommTypeForVector;
	 */
	@SuppressWarnings("rawtypes")
	public Vector rxBuffer;
	
	/**
	 * Semafor używany tutaj w roli mutexa chroniący rxBuffer przed aktualizacją w momencie gdy jest tenże
	 * rxBuffer przetwarzany przez uzytkownika w innym wątku
	 */
	public Semaphore rxBufferSemaphore = new Semaphore(1);
	
	/**
	 * Wartość RxCommType dla ostatniego transferu
	 */
	public RxCommType rxCommTypeForVector;
	
	/**
	 * Ustawiana na true żeby zasygnalizować że odbiór się zakończył i pod rxBuffer są dostępne dane.
	 * Pole powinno być kasowane na false przez jakikolwiek inną klasę która zrobi użyteg z rxBuffer aby
	 * zasygnalizować CommThread że dane są już nieistotne i mogą być nadpisane.
	 */
	public Boolean rxDataAvaliable = false;
}
