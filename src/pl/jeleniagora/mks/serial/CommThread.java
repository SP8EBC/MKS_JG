package pl.jeleniagora.mks.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fazecast.jSerialComm.SerialPort;

import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.settings.SerialCommS;
import pl.jeleniagora.mks.types.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.types.WrongInputStreamEx;

/**
 * 
 * @author mateusz
 *
 */
public class CommThread  {

	private SerialPort port;
	private Boolean isPortOpen;
	
	private InputStream rx;
	private OutputStream tx;
	
	private byte[] rxBuffer;
	
	private AnnotationConfigApplicationContext ctx;
	
	/**
	 * Wektor do trzymania danych przychodzących z portu szeregowego;
	 * @throws FailedOpenSerialPortEx 
	 */
	
	public CommThread(String portName, AnnotationConfigApplicationContext context) throws IOException, FailedOpenSerialPortEx {
		super();
		
		ctx = context;
		
		/*
		 * Tworzenie wektora bajtów pod wskazaną referencją
		 */
		rxBuffer = new byte[SerialCommS.getSerialBufferLn()];
		
		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		
//		SerialPort[] ports = SerialPort.getCommPorts();
		
		port = SerialPort.getCommPort(portName);
		port.setBaudRate(9600);
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 2345, 0);
		boolean res = port.openPort();
		
		if (res == false)
			throw new FailedOpenSerialPortEx();
		
        /*
         * Wyciąganie strumieni wejściaa wyjścia do portu szeregowego
         */
        rx = port.getInputStream();
        tx = port.getOutputStream();
        
		rte_com.port = this.port;
		rte_com.isPortOpen = true;
	}
	
	/**
	 * Metoda finalize która będzie wywoływana przez Garbage Collector przy usuwaniu obiektu z
	 * pamięci. Tutaj musi zamknąć i zwolnić port szeregowy. Metoda finalize pochodzi z java.lang.Object.finalize
	 */
	@Override
	public void finalize() {
		if (port != null && isPortOpen.equals(true))
			port.closePort();
	}
	
	public void startThreads() {
		try {
			new Thread(new Receiver(rx, rxBuffer, ctx)).start();
		} catch (WrongInputStreamEx e) {
			e.printStackTrace();
		}
	}
	 
	/**
	 * Prywatna klasa wewnętrzna która obsługuje odbiór danych z portu szeregowego
	 * @author mateusz
	 *
	 */
	private class Receiver implements Runnable {

		private InputStream str;
		
		private AnnotationConfigApplicationContext ctxInt;
		
		/**
		 * Liczba bajtów odebranych w tym odbiorze
		 */
		private int numberOfBytesRxed;
		
		private byte[] rxData;
		
		public Receiver(InputStream s, byte[] rxbuff, AnnotationConfigApplicationContext context) throws WrongInputStreamEx {
			if (s == null) {
				throw new WrongInputStreamEx();
			}
			str = s;
			numberOfBytesRxed = 0;
			rxData = rxbuff;
			ctxInt = context;
		}
		
		@Override
		public void run() {
			
			RTE_COM rte_com = ctxInt.getBean(RTE_COM.class);
			
			Thread.currentThread().setName("SerialReceiver");
			
			try {				
				/*
				 * Nieskończona pętla chodząca w wątku odbiorczym, która będzie odczytywała nadchodzące po porcie bajty.
				 * W normalnym stanie ta pętla nigdy nie powinna się zakończyć, chyba że zostanie wyłączona komunikacja po RS232
				 */
				for (;;) {
					
					/*
					 * Sprawdzanie zmiennej RTE określającej czy odbiór danych w ogóle został włączony czy nie
					 */
					if(rte_com.activateRx) {
						/*
						 * Przełączanie po różnych konfiguracjach odbioru portu szeregowego. Każda z nich wymaga nieco specyficznego
						 * podejścia do obsługi
						 */
						switch(rte_com.rxCommType) {
							case END_OF_LINE: {
								int previousNum = this.numberOfBytesRxed;
								
								/*
								 * Wywołaj blokującą funkcję odczytującą jeden bajt z portu szeregowego
								 */
								int readReturn = str.read(rxData, previousNum, 1);
								
								if(readReturn == 1) {
									/*
									 * Jeżeli odebrano jeden bajt to odczytaj go i sprawdź czy to nie jest koniec linii. Sprawdź też czy nie 
									 * dojechano do końca bufora odbiorczego
									 */
									char c = (char)(rxData[this.numberOfBytesRxed] & 0xFF);
									
									if (c == '\n' || c == '\r' || this.numberOfBytesRxed >= SerialCommS.getSerialBufferLn() + 1) {

										/*
										 * Przed dalszymi czynnościami spróbuj zasygnalizować Mutexa który tutaj jest zrealizowany jako semafor 
										 * z jednym zasobem. Zabezpieczenie przed podmianą RTE_COM.rxBuffer gdyby ten rxBuffer był używany w innym
										 * wątku
										 */
										try {
											rte_com.rxBufferSemaphore.acquire();	// uwaga! funkcja jest blokująca w nieskończoność
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										/*
										 * Jeżeli jest to bajt oznaczający koniec linii do skonwertuj odebrane dane do bufora Char
										 */										
										rte_com.rxBuffer = TypesConverters.convertByteArrayToCharVector(rxData);
										
										/*
										 * Przepis zawartość rxCommType do drugiej zmiennej "sparowanej" z buforem
										 */
										rte_com.rxCommTypeForVector = rte_com.rxCommType;
										
										/*
										 * Ustaw flagę odbioru na false aby zasygnalizować że transfer się zakończył
										 */
										rte_com.activateRx = false;
										
										/*
										 * A to ustaw na true aby zasygnalizoać że dane są dostępne
										 */
										rte_com.rxDataAvaliable = true;
										
										/*
										 * Zwalniania semafora
										 */
										rte_com.rxBufferSemaphore.release();

										/*
										 * Przeicjalizuj bufor na nowo. Stara zawartość zostanie usunięta przez Garbage Collectora
										 */
										rxData = new byte[SerialCommS.getSerialBufferLn()];
										
										/*
										 * Kasowanie liczby odebranych bajtów bo ta jest już przechowywana jako długość wektora
										 */
										this.numberOfBytesRxed = 0;

										break;
									}
									else {
										/*
										 * Jeżeli nie jest to koniec wiersza a bufor jest jescze wolny to nic nie rób
										 */
										;
									}
	
									/*
									 *	I zwiększ licznik danych odebranych. Zwiększanie odbywa się dopiero na końcu bo 
									 *	w momencie odbioru pierwszego bajtu trafia on pod indeks 0 tablicy ale funkcja zwraca naturalnie
									 *	jedynkę jako ilość odebranych danych. Jeżeli więc już w tamtym momencie ją zwiekszał o jeden to potem trzeba
									 *	by tą jedynkę odejmować żeby dostac się od ostatnio odebranego bajtu
									 *
									 *	TODO: Przeglądnąć ten kod i komentarze bo było późno jak pisałem i może być coś źle.
									 */
									this.numberOfBytesRxed++;
								
								}
								else {
									/*
									 * Jeżeli funkcja zwróciła -1 albo 0 to oznacza że po opływie timeoutu nic nie odebrano. Po prostu
									 * trzeba czekać dalej na przychodzące dane
									 */
									;
								}
								break;	// koniec case
							}
							default: break;
						}
					}
					/*
					 * Jeżeli odiór danych nie był zlecony to po prostu nic nie rób i czekaj dalej.
					 */
					else {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				;
			}
			
		}
		
	}
	
	private class Transmitter implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
