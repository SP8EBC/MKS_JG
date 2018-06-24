package pl.jeleniagora.mks.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalTime;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fazecast.jSerialComm.SerialPort;

import pl.jeleniagora.mks.exceptions.FailedOpenSerialPortEx;
import pl.jeleniagora.mks.exceptions.WrongInputStreamEx;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.settings.SerialCommS;

/**
 * 
 * @author mateusz
 *
 */
public class CommThread  {
	
	RTE_COM rte_com;
	
	String portN;
	
	private boolean _rxOnly;

	private SerialPort port;
	private Boolean isPortOpen;
	
	private InputStream rx;
	private OutputStream tx;
	
	private byte[] rxBuffer;
	
	private AnnotationConfigApplicationContext ctx;
	
	private LocalTime rxTransferStart;
	private boolean rxStartDebounce;
	
	/**
	 * Wektor do trzymania danych przychodzących z portu szeregowego;
	 * @throws FailedOpenSerialPortEx 
	 */
	
	public CommThread(String portName, RTE_COM rte, boolean rxOnly) throws IOException, FailedOpenSerialPortEx {
		super();
		
//		ctx = context;
		
		rte_com = rte;
		
		/*
		 * Tworzenie wektora bajtów pod wskazaną referencją
		 */
		rxBuffer = new byte[SerialCommS.getSerialBufferLn()];
		
//		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		
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
		
		_rxOnly = rxOnly;
		
		portN = portName;
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
			new Thread(new Receiver(rx, rxBuffer, rte_com)).start();
		} catch (WrongInputStreamEx e) {
			e.printStackTrace();
		}
		if (!_rxOnly) {
 
				new Thread(new Transmitter(tx, rte_com, portN)).start();
			
		}
	}
	 
	/**
	 * Prywatna klasa wewnętrzna która obsługuje odbiór danych z portu szeregowego
	 * @author mateusz
	 *
	 */
	private class Receiver implements Runnable {

		private InputStream str;
		
		private RTE_COM rte_com;
				
		/**
		 * Liczba bajtów odebranych w tym odbiorze
		 */
		private int numberOfBytesRxed;
		
		private byte[] rxData;
		
		public Receiver(InputStream s, byte[] rxbuff, RTE_COM rte) throws WrongInputStreamEx {
			if (s == null) {
				throw new WrongInputStreamEx();
			}
			str = s;
			numberOfBytesRxed = 0;
			rxData = rxbuff;
			rte_com = rte;
			//ctxInt = context;
		}
		
		@Override
		public void run() {
			
//			RTE_COM rte_com = ctxInt.getBean(RTE_COM.class);
			
			Thread.currentThread().setName("SerialReceiver_" + portN);
			System.out.println("--- SerialReceived started");
			
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
						if (!rxStartDebounce && !SerialCommS.isDetectTimeoutAfterFirstByte()) {
							System.out.println("-- Serial timeout debounce");
							rxTransferStart = LocalTime.now();
							rxStartDebounce = true;
						}
						
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
									
									if (!rxStartDebounce && SerialCommS.isDetectTimeoutAfterFirstByte()) {
										System.out.println("-- Serial timeout debounce");
										rxTransferStart = LocalTime.now();
										rxStartDebounce = true;
									}
									
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
										
										rxStartDebounce = false;
										rxTransferStart = null;
										
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
									 * trzeba czekać dalej na przychodzące dane. Sprawdź globalny timeout
									 */
									LocalTime actualTime = LocalTime.now();
									long diffMilis = 0;
									if (rxTransferStart != null) {
										Duration diff = Duration.between(actualTime, rxTransferStart);
										diffMilis = Math.abs(diff.toMillis());
									}
									
									if (diffMilis > SerialCommS.getMaxRxTimeoutMsec()) {
										System.out.println("-- Serial Timeout - rxed: " + this.numberOfBytesRxed + " - to be rxed: " + rte_com.numberOfBytesToRx);

										/*
										 * 
										 */
										try {
											rte_com.rxBufferSemaphore.acquire();	// uwaga! funkcja jest blokująca w nieskończoność
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										rte_com.rxBuffer = null;
										
										rte_com.rxCommTypeForVector = rte_com.rxCommType;
										
										rxData = new byte[SerialCommS.getSerialBufferLn()];

										this.numberOfBytesRxed = 0;

										/*
										 * W tym przypadku ta flaga nie zostanie ustawiona, czyli zewnętrzny proces oczekujący tych danych
										 * nie zareaguje
										 */
										rte_com.rxDataAvaliable = false;
										/*
										 * Zwalniania semafora
										 */
										rte_com.rxBufferSemaphore.release();
										
										if (SerialCommS.isAutoRestartAfterTimeout()) {
											rte_com.activateRx = true;
											rte_com.rxCommType = RxCommType.END_OF_LINE;
											rxStartDebounce = false;
											rxTransferStart = null;

										}
										else {
											rte_com.activateRx = false;
											rte_com.rxCommTypeForVector = RxCommType.END_OF_LINE;
										}
										
									}
								}
								break;	// koniec case
							}
							case NUM_OF_BYTES: {
								int previousNum = this.numberOfBytesRxed;

								int readReturn = str.read(rxData, previousNum, 1);

								if(readReturn == 1) {
									/*
									 * Jeżeli funkcja odebrała jakieś dane, zwiększ licznik odbioru
									 *
									 */
									this.numberOfBytesRxed++;

									if (!rxStartDebounce && SerialCommS.isDetectTimeoutAfterFirstByte()) {
										System.out.println("-- Serial timeout debounce");
										rxTransferStart = LocalTime.now();
										rxStartDebounce = true;
									}
									
									/*
									 * I sprawdź czy nie przekroczono już liczby bajtów do odbioru
									 * 
									 */
									if (this.numberOfBytesRxed >= rte_com.numberOfBytesToRx) {
										System.out.println("-- Serial transfer done");
										/*
										 * Jeżeli przekroczono
										 */
										try {
											rte_com.rxBufferSemaphore.acquire();	// uwaga! funkcja jest blokująca w nieskończoność
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
																	
										rte_com.rxBuffer = TypesConverters.convertByteArrayToCharVector(rxData);
										
										/*
										 * Przepis zawartość rxCommType do drugiej zmiennej "sparowanej" z buforem
										 */
										rte_com.rxCommTypeForVector = rte_com.rxCommType;
										
										/*
										 * Ustaw flagę odbioru na false aby zasygnalizować że transfer się zakończył
										 */
										rte_com.activateRx = false;
										
										rxStartDebounce = false;
										rxTransferStart = null;
										
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
										; // odbieraj dalej
									}
									
								}
								else {
									/*
									 * Sprawdź globalny timeout
									 */
									LocalTime actualTime = LocalTime.now();
									long diffMilis = 0;
									if (rxTransferStart != null) {
										Duration diff = Duration.between(actualTime, rxTransferStart);
										diffMilis = Math.abs(diff.toMillis());
									}
									
									if (actualTime != null && diffMilis > SerialCommS.getMaxRxTimeoutMsec()) {
										System.out.println("-- Serial Timeout - rxed: " + this.numberOfBytesRxed + " - to be rxed: " + rte_com.numberOfBytesToRx);
										/*
										 * 
										 */
										try {
											rte_com.rxBufferSemaphore.acquire();	// uwaga! funkcja jest blokująca w nieskończoność
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										
										rte_com.rxBuffer = null;
										
										rte_com.rxCommTypeForVector = rte_com.rxCommType;
										
										rxData = new byte[SerialCommS.getSerialBufferLn()];

										this.numberOfBytesRxed = 0;

										/*
										 * W tym przypadku ta flaga nie zostanie ustawiona, czyli zewnętrzny proces oczekujący tych danych
										 * nie zareaguje
										 */
										rte_com.rxDataAvaliable = false;
										/*
										 * Zwalniania semafora
										 */
										rte_com.rxBufferSemaphore.release();
										
										if (SerialCommS.isAutoRestartAfterTimeout()) {
											rte_com.activateRx = true;
											rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
											rxStartDebounce = false;
											rxTransferStart = null;

										}
										else {
											rte_com.activateRx = false;
											rte_com.rxCommTypeForVector = RxCommType.NUM_OF_BYTES;
										}
										
									}
								}
								
								break;
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
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				;
			}
			
		}
		
	}
	
	private class Transmitter implements Runnable {

		private OutputStream str;
		
		private RTE_COM rte_com;
		
		/**
		 * Liczba bajtów do wysłania przez port szeregowy
		 */
		private int dataLnToTx;
		
		/**
		 * Bufor na dane do wysłania
		 */
		private byte[] txData;
		
		String portName;
		
//		private AnnotationConfigApplicationContext ctxInt;
		
		public Transmitter(OutputStream s, RTE_COM rte, String port) {
			str = s;
			//ctxInt = context;
			rte_com = rte;
			portName = port;
		}
		
		@Override
		public void run() {
			
//			RTE_COM rte_com = ctxInt.getBean(RTE_COM.class);
			
			Thread.currentThread().setName("SerialTransmitter_" + portN);
			System.out.println("--- SerialTransmitter started");
			
			for (;;) {
				if (rte_com.activateTx) {					
					/*
					 * Jeżeli zlecono nadanie jakichś danych po porcie szeregowym
					 */
					try {
						rte_com.txBuferSemaphore.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// konwersja wektora na tablice
					this.txData = TypesConverters.convertByteVectorToByteArray(rte_com.txBuffer);
					this.dataLnToTx = this.txData.length;
					
					System.out.println("-- Serial sending " + this.dataLnToTx  + " bytes of data over: " + this.portName);
					
					try {
						str.write(txData, 0, dataLnToTx);
					} catch (IOException e) {
						// TODO Coś tu zrobić na ewentualność że wysyłanie może się nie udać
						e.printStackTrace();
						rte_com.activateTx = false;
					}
					
					if (rte_com.waitAfterTx > 0) {
						try {
							Thread.sleep(rte_com.waitAfterTx);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					rte_com.txBuferSemaphore.release();	// zwalnianie semafora po zakończonym odczycie
					rte_com.activateTx = false;
					
					System.out.println("-- Serial done sending " + this.dataLnToTx  + " bytes of data over: " + this.portName);
				}
				else {
					/*
					 * Jeżeli nie to czekaj 1ms i sprawdzaj dalej
					 */
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
	}

}
