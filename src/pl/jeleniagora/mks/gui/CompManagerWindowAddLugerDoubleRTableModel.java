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
		return chosenCompetition.competitorsCount;
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
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
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
