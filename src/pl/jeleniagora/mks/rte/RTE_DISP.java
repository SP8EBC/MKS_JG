package pl.jeleniagora.mks.rte;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_DISP {

	/**
	 * Ustawienie na true powoduje, że program będzie automatycznie wyświetlał kolejnego zawodnika po 
	 * zakończonym ślizgu poprzedniego. Ustawienie na false spowoduje że ekran będzie wygaszany
	 */
	public boolean autoShowNextLuger = true;
	
	public boolean autoShowRuntimeAfterLanding = true;
	
	/**
	 * Podtrzymanie wyświetlania czasu ślizgu po naciśnięciu przycisku "Zapisz czas zawodnika"
	 */
	public short displayRuntimeAndRankDelayAfterSaving = 9000;
	
	/**
	 * Podtrzymanie wyświetlania czasu ślizgu po dojechaniu zawodnika do mety
	 */
	public short displayRuntimeDelayAfterLanded = 2500;
}
