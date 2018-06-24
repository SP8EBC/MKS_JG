package pl.jeleniagora.mks.chrono;

import java.time.LocalTime;
import java.util.Vector;

import static java.time.temporal.ChronoUnit.NANOS;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.serial.RxCommType;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.ChronometerType;
import pl.jeleniagora.mks.types.Reserve;

/**
 * Główna klasa obsługująca wątek służący do pomiar czasu ślizgu
 * @author mateusz
 *
 */
public class Chrono implements Runnable {

	private Parser parser;
	private ChronoStateMachine timeMeasurementState;
	
	private GateTimeData 	v1Timestamp, 		// pierwsza fotocela (Start)
							airbourneTimestamp, // druga fotocela (pierwszy pomiar pośredni)
							enrouteTimestamp, 	// trzecia fotocela (drugi pomiar pośredni)
							onfinalTimestamp,	// czwarta fotocela (trzeci pomiar pośredni)
							landedTimestamp;	// piąta fotocela (pomiar na mecie)
	
	private AnnotationConfigApplicationContext ctx;
	
	public Chrono(AnnotationConfigApplicationContext context) {
		timeMeasurementState = ChronoStateMachine.IDLE;
		
		this.ctx = context;
		
		switch (ChronometerS.chronometerType) {
		case SECTRO: {
			parser = new SectroParser();
		}
		}
	}

	
	/**
	 * Wątek uruchamiany co ~10msec który sprawdza czy nie przyszły nowe dane z chronometru
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		RTE_COM rte_com = (RTE_COM)ctx.getBean("comBean");
		RTE_CHRONO rte_chrono = ctx.getBean(RTE_CHRONO.class);

		Thread.currentThread().setName("Chrono");
		
		/*
		 * Wątek chodzi w nieskończonej pętli ze sleep na końcu, co zabezpiecza przed CPU load 100%
		 */
		for (;;) {
			
			/*
			 * Sprawdzenie czy nie wystawiono flagi resetującej maszynę stanu
			 */
			if (rte_chrono.resetStateMachine) {
				timeMeasurementState = ChronoStateMachine.IDLE; 
				
				rte_chrono.resetStateMachine = false;
			}
			/*
			 * Sprawdzanie czy z portu szeregowego przyszły jakieś nowe dane
			 */
			if (rte_com.rxDataAvaliable) {
				System.out.println("-- rx data avaliable");
				
				GateTimeData td;
				
				/*
				 * Sekcja krytyczna
				 */
				try {
					rte_com.rxBufferSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (rte_com.rxBuffer == null) {
					rte_com.rxDataAvaliable = false;
					rte_com.rxBufferSemaphore.release();
					
					rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
					rte_com.numberOfBytesToRx = 14;
					rte_com.activateRx = true;

					
					continue;
				}
				Vector<Character> rxData_rc = new Vector<Character>(rte_com.rxBuffer);
				rte_com.rxDataAvaliable = false;
				
				rte_com.rxBufferSemaphore.release();
				
				/*
				 * Koniec sekcji krytycznej
				 */
				
				/*
				 * Parsowanie danych otrzymanych po RSie
				 */
				try {
					td = parser.parseFromSerialData(rxData_rc);
				} catch (Reserve e) {
					e.printStackTrace();
					rte_chrono.failToParseData = true;
					System.out.println("-- failed to parse data");
					
					rte_com.activateRx = true;
					rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
					rte_com.numberOfBytesToRx = 14;
	
					synchronized(rte_chrono.syncError) {
						rte_chrono.syncError.notifyAll();
					}
					
					continue;
				}
				
				rte_chrono.failToParseData = false;				
				/*
				 * Koniec parsowania
				 */
				
				/*
				 * Maszyna stanów
				 */
				switch(timeMeasurementState) {
				case IDLE: 			timeMeasurementState = ChronoStateMachine.LINE_UP;	// brak break jest tutaj zamierzony
				case LINE_UP: {
					if (ChronometerS.gateCount == 2 && td.getGateId() == 1) {
						timeMeasurementState = ChronoStateMachine.V1_ROTATE; 
						v1Timestamp = td;		// zapisywanie timestampu przejśćia w stane V1_ROTATE czyli czasu startu
						System.out.println("-- swtiched to V1_ROTATE state with timestamp " + td.getTime().toString());
					}
					else;
					break;
				}
				case CLEAR_FOR_TAKEOFF: break;
				case V1_ROTATE: {
					if (ChronometerS.gateCount == 2 && td.getGateId() == 3) {
						timeMeasurementState = ChronoStateMachine.LANDED; 
						landedTimestamp = td;
						System.out.println("-- swtiched to LANDED state with timestamp " + td.getTime().toString());
					}
					break;
				}
				case AIRBORNE: 			break;
				case EN_ROUTE:			break;
				case ON_FINAL:			break;
				case LANDED: {
					break;
				}

				default:				break;
				}
				
				try {
					rte_chrono.timeMeasSemaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				rte_chrono.timeMeasState = timeMeasurementState;
				rte_chrono.timeMeasSemaphore.release();

				synchronized (rte_chrono.syncState) {
					rte_chrono.syncState.notifyAll();
				}
				
				/*
				 * Obliczanie czasów
				 */
				if (timeMeasurementState == ChronoStateMachine.LANDED) {
					LocalTime runTime;
					LocalTime startTime = v1Timestamp.getTime();
					LocalTime endTime = landedTimestamp.getTime();
					
					if (startTime != null && endTime != null) {
						long diff = startTime.until(endTime, NANOS);
						runTime = LocalTime.ofNanoOfDay(diff);
						rte_chrono.runTime = runTime;

						try {
							LandedStateReached.process(runTime);
						} catch (AppContextUninitializedEx e) {
							e.printStackTrace();
						}
						
						synchronized(rte_chrono.syncRuntime) {
							rte_chrono.syncRuntime.notifyAll();
						}
						
						System.out.println("-- runtime " + runTime.toString());
					}
					
					
					timeMeasurementState = ChronoStateMachine.IDLE; 
				}
				rte_com.rxCommType = RxCommType.NUM_OF_BYTES;
				rte_com.numberOfBytesToRx = 14;
				rte_com.activateRx = true;
			
			}
			else {

			}
			
			/*
			 * Usypianie wątku na 10msec
			 */
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
