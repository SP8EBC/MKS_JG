package pl.jeleniagora.mks.rte;

import java.time.LocalTime;
import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.chrono.ChronoStateMachine;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_CHRONO {
	
	RTE_CHRONO() {
		this.timeMeasSemaphore = new Semaphore(1);
		this.failToParseData = false;
		this.timeMeasState = ChronoStateMachine.IDLE;
		this.syncState = new Object();
		this.syncRuntime = new Object();
		this.syncError = new Object();
		this.terminateChronoThread = false;
		
		startSignalId = new Integer(1);
		endSignalId = new Integer(3);
	}
	
	public Object syncState;
	public Object syncRuntime;
	public Object syncError;
	
	public LocalTime runTime;
	
	public ChronoStateMachine timeMeasState;
	
	public Semaphore timeMeasSemaphore;
	
	public boolean failToParseData;
	
	public boolean resetStateMachine;
	
	public boolean terminateChronoThread;
	
	/**
	 * Ustawienie na true powoduje że każde przecięcie fotoceli startu będzie powodowało 
	 */
	public boolean restartOnEachStartSignal;
	
	public Integer startSignalId;
	
	public Integer endSignalId;
}
