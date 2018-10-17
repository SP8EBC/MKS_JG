package pl.jeleniagora.mks.gui;

import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerSingle;

public class CompManagerWindowAddLugerSingleRTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1995704135609847357L;

	Competition chosenCompetition;
	Vector<LugerCompetitor> displayVct;
	
	public CompManagerWindowAddLugerSingleRTableModel (Competition in) {
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
		// TODO Auto-generated method stub
		return displayVct.size();
	}
	
	@Override
	public String getColumnName(int c) {
		if (c == 0) 
			return "Imię";
		else
			return "Nazwisko";
		
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		if (arg0 < 0 || arg1 < 0)
			return null;
		
		LugerCompetitor k = null;
		
		try {
			k = displayVct.elementAt(arg0);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return "";
		}
		LugerSingle s = (LugerSingle)k;
		
		if (arg1 == 0) {
			return s.single.name;
		}
		if (arg1 == 1) {
			return s.single.surname;
		}
		
		return null;
	}

}
