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
		displayVct = new Vector<LugerCompetitor>();
		
		for (Entry<LugerCompetitor, Short> e : chosenCompetition.startList.entrySet()) {
			LugerCompetitor k = e.getKey();
			displayVct.addElement(k);
		}
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return chosenCompetition.competitorsCount; // TODO: sprawdzić czy to pole jest włściwie aktualizowane
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
		LugerCompetitor k = displayVct.elementAt(arg0);
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
