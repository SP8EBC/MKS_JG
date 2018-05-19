package pl.jeleniagora.mks.rte;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.gui.CompManagerScoreTableModel;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionEncapsulationForSelector;

/**
 * Klasa będąca odpowiednikiem AUTOSARowego Rutime Environment, w tym przypadku przechowuje statyczne referencje do obiektów obsługujących
 * poszczególne okienka interfejsu. Dzięki temu np. Action Listenery wyciągnięte do zewnętrznych klas mogą mieć łatwy dostep do danych
 * tych obiektów. Daje to możliwość np zmiany zawartości jakiejś JTable albo JLabel po kliknięciu na przycisk itp. 
 * 
 * Oczywiście ponieważ pola ten klasy są statycznie nie da się tutaj zapisać więcej niż jednej referencji co nie jest jednak problemem ponieważ
 * np. okno obsługi zawodów może być wywołane tylko raz i istnieć tylko w jednej i tej samej instacji.
 * 
 * @author mateusz
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RTE_GUI {
	public CompManager compManager;
	
	public JComboBox<CompetitionEncapsulationForSelector> compManagerCSelector;
	
	/* 
	 * Model używany do rysowania listy wników w obsłudze zawodów i treningów. Jest tu potrzebna gdyż w tej klasie siedzi metoda aktualizująca
	 * liczbę i typ kolumn oraz dane w tabeli 
	 */
	public CompManagerScoreTableModel compManagerScoreModel;
	
	public JTextField min;
	public JTextField sec;
	public JTextField msec;
	
	public JLabel actuallyOnTrack;
	public JLabel nextOnTrack;
	public JLabel currentCompetition;
	
	public Object syncCompManagerRdy;
	
	public Competition competitionBeingShown;
}
