package pl.jeleniagora.mks.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Luger;

/**
 * Model danych dla lewej tabeli w oknie Dodawania/usuwania zawodnika z konkurencji jedynek
 * @author mateusz
 *
 */
@Component
public class CompManagerWindowAddLugerSingleLTableModel extends AbstractTableModel {

	@Autowired
	RTE_ST rte_st;
	
	List<Luger> listOfLugersToShow;
	
	Object[][] tableData;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5250867278266583242L;

	public void updateContent(boolean onlyNotAdded) {
		int i = 0;
		
		List<Luger> inputList = rte_st.competitions.listOfAllLugersInThisCompetitions;
		listOfLugersToShow = new LinkedList<Luger>();
		
		for (Luger elem: inputList) {
			if (onlyNotAdded && elem.hasBeenAdded)
				continue;
			else {
				listOfLugersToShow.add(elem);
			}
		}
		
		tableData = new String[listOfLugersToShow.size()][2];
		
		for (Luger e : listOfLugersToShow) {
			tableData[i][0] = e.name;
			tableData[i][1] = e.surname;
			
			i++;
		}
		
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return listOfLugersToShow.size();
	}

	public Object[][] getTableData() {
		return tableData;
	}
	
	@Override
	public Object getValueAt(int arg0, int arg1) {
		String name, surname;
		
		Luger elem = listOfLugersToShow.get(arg0);
		
		if (elem != null) {
			name = elem.name;
			surname = elem.surname;
		}
		else {
			name = "";
			surname = "";
		}
		
		if (arg1 == 0) {
			return name;
		}
		else if (arg1 == 1) {
			return surname;
		}
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
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
}
