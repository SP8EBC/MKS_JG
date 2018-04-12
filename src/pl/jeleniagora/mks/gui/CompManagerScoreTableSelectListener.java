package pl.jeleniagora.mks.gui;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Listener wyzwalany w momencie kliknięcia przez użytkonika na jakąś komórkę głównej tabeli z wynikami ślizgów
 * @author mateusz
 *
 */
public class CompManagerScoreTableSelectListener implements ListSelectionListener {

	AnnotationConfigApplicationContext ctx;

	
	/**
	 * Referencja do obiektu JTable z główną tabeli wyników. Ustawiana przez konstruktora
	 */
	private JTable t;
	
	public CompManagerScoreTableSelectListener(JTable table, AnnotationConfigApplicationContext context) {
		t = table;
		ctx = context;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		
		@SuppressWarnings("unused")
		Integer modelRow, modelColumn;
		
		/* 
		 * Wyciąganie numeru wiersza i kolumny, którą kliknął użytkownik. Nalezy pamiętać
		 * że numeracja idzie tutaj od zera a nie od jedynki!
		 * 
		 * WAŻNA UWAGA! Obydwie poniższe metody zwracają jedynie pozycję kliknięcia w tabeli a nie pozycję
		 * danych w tablicy te dane przechowywującej. Jeżeli użytkownik zaczał używać sortowania danych to najczęściej
		 * wiersze zostały przetasowane i kolejność wyświetlana a kolejność w tablicy już sobie nie odpowiadają.
		 * 
		 * Aby skutecznie odwołać się do konkretnych danych, które kliknął użytkownik należy użyć odpowiednich metod, które
		 * dokonują potrzebnej translacji.
		 */
		Integer r = t.getSelectedRow();
		Integer c = t.getSelectedColumn();
		
		/*
		 * Translacja współrzędnych wiersza i kolumny z widoku na pozycję w modelu, czyli w tablicy przechowywującej
		 * dane wyświetlane przez JList
		 */
		if (r >= 0 && c >= 0) {
			modelRow = t.convertRowIndexToModel(r);
			modelColumn = t.convertColumnIndexToModel(c);
		}
		
		@SuppressWarnings("unused")
		DefaultListSelectionModel eventSource = (DefaultListSelectionModel)arg0.getSource();
		return;
		// TODO Auto-generated method stub
		
	}

}
