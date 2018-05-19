package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.CompetitionEncapsulationForSelector;
import pl.jeleniagora.mks.types.UninitializedCompEx;

/**
 * Klasa obsługująca akcję od listy rozwijanej z wszystkimi konkurencjami w oknie "Obsługa Zawodów i Treningów"
 * @author mateusz
 *
 */
public class CompManagerCSelectorActionListener implements ActionListener {

	CompManagerScoreTableModel mdl;
	
	AnnotationConfigApplicationContext ctx;
	
	CompManagerCSelectorActionListener(CompManagerScoreTableModel model, AnnotationConfigApplicationContext context) {
		mdl = model;
		ctx = context;
		}
	
	/**
	 * Metoa wywoływana po akcji na polu rozwijanym do wyboru konkurencji, w zasadzie tutaj jedyną akcją
	 * będzie zmiana pozycji. W związku z tym metoda actionPerformed obsługuje zmianę konkurencji wyświetlanej w głównej
	 * tabeli wyników
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object selectorObj = arg0.getSource();
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		if (selectorObj instanceof JComboBox<?>) {
			/*
			 * Przy tym rzutowaniu nie da się uniknąć ostrzeżenia o niesprawdzonym rzutowaniu bo JComboBox jest to
			 * generic class.
			 */
			@SuppressWarnings("unchecked")
			JComboBox<CompetitionEncapsulationForSelector> selector = (JComboBox<CompetitionEncapsulationForSelector>) selectorObj;
			
			/*
			 * Wyciąganie wskazanego elementu
			 */
						
			if (selector.getSelectedItem() != null) {
				
				Competition selected = ((CompetitionEncapsulationForSelector)selector.getSelectedItem()).getCompetition();

				rte_gui.competitionBeingShown = selected;
								
				// TODO: Umożliwić wykorzystanie czasów pośrednich
				try {
					mdl.updateTableData(selected, false);
				} catch (UninitializedCompEx e) {
					e.printStackTrace();
				}
				
				mdl.fireTableStructureChanged();
			}
			
			return;
		}
		else return;
		
	}

}
