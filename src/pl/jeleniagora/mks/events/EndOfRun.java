package pl.jeleniagora.mks.events;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.files.ScoreTableCsvSaver;
import pl.jeleniagora.mks.reports.CompetitionReportGenerator;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.scoring.CalculateRanksAfterRun;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa zawierąca metody wywoływane po ostatim saneczkarzu w ślizgu
 * @author mateusz
 *
 */
public class EndOfRun {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	/**
	 * Prywatny konstruktor który ma uniemożliwić stworzenie egzemplarza tej klasy
	 */
	private EndOfRun() {
		
	}
	
	public static void process() throws EndOfCompEx {
		calculateRanks();
				
		saveTableToCsv();
		
		try {
			switchToNextRun();
		}
		catch (EndOfCompEx e) {
			generateFinalReport();			
			throw e;
		}
		
		generatePartialReport();
	}
	
	static void saveTableToCsv() {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		if (rte_st.filePath == null)
			return;
		
		Path path = Paths.get(rte_st.filePath + "pliki_csv/");
		
		// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
		if (!Files.exists(path)) {
			// jeżeli nie istnieje to należy go utworzyć
			new File(rte_st.filePath + "pliki_csv/").mkdirs();
		}
		
		String fn = rte_st.competitions.toString() + "___" + rte_st.currentCompetition.toString().replaceAll(" ", "_");
		fn += ("__po_zakoczeniu__" + rte_st.currentRun.toString().replaceAll(" ", "_") + ".csv");
		
		ScoreTableCsvSaver csv = new ScoreTableCsvSaver(rte_st.filePath + "pliki_csv/" + fn);
		try {
			csv.saveTableToFile(rte_gui.model.getColumnNames(), rte_gui.model.getTableData(), rte_gui.model.getTypes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void generatePartialReport() {
		RTE_ST st = (RTE_ST)ctx.getBean("RTE_ST");
		
		CompetitionReportGenerator gen = new CompetitionReportGenerator(st.currentCompetition, st.competitions, st.filePath + "raporty/");
		
		Path path = Paths.get(st.filePath + "raporty_czesciowe/");
		
		// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
		if (!Files.exists(path)) {
			// jeżeli nie istnieje to należy go utworzyć
			new File(st.filePath + "raporty/").mkdirs();
		}
		
		try {
			gen.generate(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void generateFinalReport() {
		RTE_ST st = (RTE_ST)ctx.getBean("RTE_ST");
		
		CompetitionReportGenerator gen = new CompetitionReportGenerator(st.currentCompetition, st.competitions, st.filePath + "raporty/");
		
		Path path = Paths.get(st.filePath + "raporty_czesciowe/");
		
		// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
		if (!Files.exists(path)) {
			// jeżeli nie istnieje to należy go utworzyć
			new File(st.filePath + "raporty/").mkdirs();
		}
		
		try {
			gen.generate(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void calculateRanks() {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		
		Map<LugerCompetitor, LocalTime> totalTimes = calc.calculateTotalRuntime(rte_st.currentCompetition);
		if (totalTimes == null)
			return;
		
		Map<LugerCompetitor, Short> ranks = calc.calculateRanksFromTotalRuntimes(totalTimes);
		
		rte_st.currentCompetition.ranks = ranks;
		try {
			rte_gui.compManagerScoreModel.updateTableData(rte_st.currentCompetition, false);
		} catch (UninitializedCompEx e) {
			e.printStackTrace();
		}
	}
	
	static void switchToNextRun() throws EndOfCompEx {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		int numberOfRuns = rte_st.currentCompetition.numberOfAllRuns;
		
		rte_st.currentRun.done = true;
		
		if (rte_st.currentRunCnt + 1 < numberOfRuns) {
			/*
			 * Jeżeli w tej konkurencji jest jeszcze jakiś ślizg
			 */
			
			rte_st.currentCompetition.partialRanks = new HashMap<LugerCompetitor, Short>();
			
			rte_st.currentRunCnt++;
			
			rte_st.currentRun = rte_st.currentCompetition.runsTimes.elementAt(rte_st.currentRunCnt);
			
			// sprawdzanie czy aktualny ślizg jest treningiem czy nie
			if (rte_st.currentRun.trainingOrScored)
				rte_st.currentCompetition.isCurrentRunTraining = false;
			else
				rte_st.currentCompetition.isCurrentRunTraining = true;
			
			// sprawdzenie czy aktualny ślizg jest pierwszym punktownm czy kolejnym punktowanym
			if (rte_st.currentRun.numberInScoredOrTrainingRuns > 0 && rte_st.currentRun.trainingOrScored) {
				rte_st.currentCompetition.isCurrentRunFirstScored = false;
			}
			else {
				rte_st.currentCompetition.isCurrentRunFirstScored = true;
			}
			
			/*
			 * ustawia też: runClickedInTable oraz competitorClickedInTable
			 */
			UpdateCurrentAndNextLuger.rewindToBegin();
			/*
			 * 
			 */
			rte_gui.compManagerScoreModel.fireTableStructureChanged();
			rte_gui.compManagerScoreModel.fireTableDataChanged();
			rte_gui.compManager.sortByStartNumberAscending();
			
			/*
			 * Czasami po zastosowaniu autosortowania tabeli po zakończonym ślizgu "odklikiwał się"
			 * pierwszy zawodnik i przeskakiwał na ostatniego wyświetlanego w tabeli. Poniższe
			 * przypisanie jest bufixem na tą przypadłość. 
			 */
			rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
			

		}
		else {
			/*
			 * Jeżeli nie to znaczy że to koniec konkurencji
			 */
			rte_st.returnComptr = null;
			rte_st.currentRun = null;
			rte_st.nextOnTrack = null;
			rte_st.actuallyOnTrack = null;
			rte_st.currentCompetition = null;
			
			rte_gui.currentCompetition.setText("---");
			rte_gui.actuallyOnTrack.setText("---");
			rte_gui.nextOnTrack.setText("---");
			
			JOptionPane.showMessageDialog(null, "Konkurencja zakończyła się");
			throw new EndOfCompEx();
		}
		
		
	}
	
	static void switchStartOrder() {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		// sprawdzanie czy użytkownik nie zmienił w konfiguracji konkurencji kolejności startowej
		if (rte_st.currentCompetition.startOrder != rte_st.currentCompetition.startOrderToChange) {
			rte_st.currentCompetition.startOrder = rte_st.currentCompetition.startOrderToChange;
		}
		else {
			;
		}
	}
}
