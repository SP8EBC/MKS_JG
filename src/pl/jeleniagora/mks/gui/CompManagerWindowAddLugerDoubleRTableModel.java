package pl.jeleniagora.mks.gui;

import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

public class CompManagerWindowAddLugerDoubleRTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1995704135609847357L;

	Competition chosenCompetition;
	Vector<LugerCompetitor> displayVct;
	
	public CompManagerWindowAddLugerDoubleRTableModel (Competition in) {
		chosenCompetition = in;
		
		if (chosenCompetition == null) {
			return;
		}
		
		displayVct = new Vector<LugerCompetitor>();
		
		for (Entry<LugerCompetitor, Short> e : chosenCompetition.startList.entrySet()) {
			if (e.getKey() == null || e.getValue() == null) {
				// sprawdzenie to jest zabezpieczeniem przed anormalną sytuacją gdyby z jakiegoś powodu w start liście znajdował się
				// klucz albo wartość null. HashMap zezwala przechowywanie i mapowanie nulli ale gdyby próbować to dodać do modelu spowoduje
				// to NullPointerException gdyż nie da się nulla w żaden sposób wyświetlić
				// 
				continue;
			}
			LugerCompetitor k = e.getKey();
			displayVct.addElement(k);
		}
		
		return;
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		// zamienione na zwracanie rozmiaru tego wektora a nie ilości elementów w start liście
		return displayVct.size();
	}
	
	@Override
	public String getColumnName(int c) {
		if (c == 0) 
			return "Góra";
		else
			return "Dół";
		
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if (arg0 < 0 || arg1 < 0)
			return null;
		
		LugerCompetitor k = null;
		
		try {
			k = displayVct.elementAt(arg0);
		}
		// wyjątek może zostać rzucony teoretycznie wtedy gdyby Swing próbował wyświetlić element leżący poza wektorem
		catch (Exception e) {
			System.out.println(e.getMessage());
			// metoda nie może zwrócić null gdyż spowodouje wysypanie się to rutyn Swinga odpowiadających za 
			// wyświetlanie JTable
			//return null;
			return "";
		}
		LugerDouble d = (LugerDouble)k;
		
		if (arg1 == 0) {
			return d.upper.surname;
		}
		if (arg1 == 1) {
			return d.lower.surname;
		}
		
		return null;
	}

}
