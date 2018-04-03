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
	}
	
	public LocalTime runTime;
	
	public ChronoStateMachine timeMeasState;
	
	public Semaphore timeMeasSemaphore;
	
	public boolean failToParseData;
}
