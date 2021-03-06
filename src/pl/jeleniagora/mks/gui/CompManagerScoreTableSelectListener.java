package pl.jeleniagora.mks.gui;

import java.time.LocalTime;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.DisplayS;
import pl.jeleniagora.mks.types.LugerCompetitor;

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
		Integer modelRow = 0, modelColumn = 0;
				
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
				
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
		
		short startNum = (Short)rte_gui.model.getValueAt(modelRow, 0);
		String name = (String)rte_gui.model.getValueAt(modelRow, 3);
		String surname = (String)rte_gui.model.getValueAt(modelRow, 4);
		
		/*
		 * Przed wygenerowaniem numerów startowych invertedStartList jest całkowicie pusta i nie można
		 * jej używać do wyciągania klikniętego sankarza. Zamiast tego należy przeszukać po Imieniu i Nazwisku
		 */
		if (rte_gui.competitionBeingShown.invertedStartList.containsKey(startNum)) {
			rte_gui.competitorClickedInTable = rte_gui.competitionBeingShown.invertedStartList.get(startNum);
		}
		else {
			LugerCompetitor clicked = rte_gui.competitionBeingShown.findByNameAndSurname(name, surname);
			rte_gui.competitorClickedInTable = clicked;
		}
				
		if (modelColumn > DisplayS.columnOffset - 1) {
			/*
			 * Jeżeli kliknięto kolumne z czasem
			 */
			
			boolean isTrainingClicked = false;
			int trainingRunsNum = rte_gui.competitionBeingShown.numberOfTrainingRuns;
			int scoredRunsNum = rte_gui.competitionBeingShown.numberOfAllRuns - trainingRunsNum;
			
			if (modelColumn > DisplayS.columnOffset - 1 && modelColumn < DisplayS.columnOffset - 1 + trainingRunsNum) {
				/*
				 * Kliknięto jakiś ślizg treningowy
				 */
				rte_gui.runClickedInTable = rte_gui.competitionBeingShown.runsTimes.get(modelColumn - DisplayS.columnOffset);

				
			}
			else {
				/*
				 * Kliknięto jakiś ślizg punktowany
				 */
				rte_gui.runClickedInTable = rte_gui.competitionBeingShown.runsTimes.get(modelColumn - DisplayS.columnOffset);
			}
			System.out.println("Clicked run: " + rte_gui.runClickedInTable.toString());
			
		}
		
		if (
			(rte_gui.competitorPreviouslyClicked != rte_gui.competitorClickedInTable) ||
			(rte_gui.runClickedInTable != rte_gui.runPreviouslyClicked)
			) 
		{
			rte_gui.runtimeFromChrono = false;
			
			if (modelColumn >= DisplayS.columnOffset)
				updateTextFieldsInCM(rte_gui.competitionBeingShown.runsTimes.get(modelColumn - DisplayS.columnOffset).totalTimes.get(rte_gui.competitorClickedInTable));
			
			System.out.println("runtimeFromChrono = false");
		}
		
		rte_gui.competitorPreviouslyClicked = rte_gui.competitorClickedInTable;
		rte_gui.runPreviouslyClicked = rte_gui.runClickedInTable;
		
		@SuppressWarnings("unused")
		DefaultListSelectionModel eventSource = (DefaultListSelectionModel)arg0.getSource();
		return;
		
	}
	
	void updateTextFieldsInCM(LocalTime runTime) {
		
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		Integer min, sec, msec;
		
		min = runTime.getMinute();
		sec = runTime.getSecond();
		msec = runTime.getNano() / TypesConverters.nanoToMilisecScaling;
		
		String minString = min.toString();
		
		rte_gui.min.setText(minString);
		rte_gui.sec.setText(sec.toString());
		rte_gui.msec.setText(msec.toString());
	}

}
