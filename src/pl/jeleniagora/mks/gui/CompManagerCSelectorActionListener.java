package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.UninitializedCompEx;

/**
 * Klasa obsługująca akcję od listy rozwijanej z wszystkimi konkurencjami w oknie "Obsługa Zawodów i Treningów"
 * @author mateusz
 *
 */
public class CompManagerCSelectorActionListener implements ActionListener {

	CompManagerScoreTableModel mdl;
	
	CompManagerCSelectorActionListener(CompManagerScoreTableModel model) {mdl = model;}
	
	/**
	 * Metoa wywoływana po akcji na polu rozwijanym do wyboru konkurencji, w zasadzie tutaj jedyną akcją
	 * będzie zmiana pozycji. W związku z tym metoda actionPerformed obsługuje zmianę konkurencji wyświetlanej w głównej
	 * tabeli wyników
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object selectorObj = arg0.getSource();
		
		if (selectorObj instanceof JComboBox<?>) {
			/*
			 * Przy tym rzutowaniu nie da się uniknąć ostrzeżenia o niesprawdzonym rzutowaniu bo JComboBox jest to
			 * generic class.
			 */
			@SuppressWarnings("unchecked")
			JComboBox<Competition> selector = (JComboBox<Competition>) selectorObj;
			
			/*
			 * Wyciąganie wskazanego elementu
			 */
			Competition selected = (Competition)selector.getSelectedItem();
						
			// TODO: Umożliwić wykorzystanie czasów pośrednich
			try {
				mdl.updateTableData(selected, false);
			} catch (UninitializedCompEx e) {
				e.printStackTrace();
			}
			
			mdl.fireTableStructureChanged();
			
			return;
		}
		else return;
		
	}

}
