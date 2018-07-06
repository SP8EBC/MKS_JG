package pl.jeleniagora.mks.gui;

import javax.swing.table.AbstractTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;

/**
 * Model danych dla lewej tabeli w oknie Dodawania/usuwania zawodnika z konkurencji jedynek
 * @author mateusz
 *
 */
@Component
public class CompManagerWindowAddLugerSingleLTableModel extends AbstractTableModel {

	@Autowired
	RTE_ST rte_st;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5250867278266583242L;

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class getColumnClass(int c) {
		return new String().getClass();
		
	}
	
	@Override
	public String getColumnName(int c) {
		if (c == 0) 
			return "Imię";
		else
			return "Nazwisko";
		
	}
	
}
