package pl.jeleniagora.mks.events;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.display.DisplayRuntimeAndRank;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.NoMoreCompetitionsEx;
import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.files.ScoreTableCsvSaver;
import pl.jeleniagora.mks.files.xml.XmlSaver;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_DISP;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.scoring.CalculatePartialRanks;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.GeneralS;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa służy do zapisu czasu ślizgu z poziomu dodatkowego wątku, który będzie opóźniał to o kilka sekund na potrzeby opcji 
 * "Autozapis czasu ślizgu"
 * 
 * Używana w klasie LandedStateReached jeżeli ChronometerS.autosave jest ustawione na true
 * 
 * @author mateusz
 *
 */
public class SaveRuntimeDelayThread implements Runnable {

	private AnnotationConfigApplicationContext ctx;
	private LocalTime rt;
	private boolean inhibitMsg;
	
	/**
	 * Konstruktor ustawiający 
	 * @param context
	 */
	public SaveRuntimeDelayThread(AnnotationConfigApplicationContext context, LocalTime runtime, boolean inhibitMessages) {
		ctx = context;
		rt = runtime;
		inhibitMsg = inhibitMessages;
	}
	
	/**
	 * Punkt wejścia do wątku
	 */
	public void run() {
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_DISP rte_disp = (RTE_DISP)ctx.getBean("RTE_DISP");
		XmlSaver saver = (XmlSaver)ctx.getBean(XmlSaver.class);
		
		System.out.println("SaveRuntimeDelayThread started");
		
		boolean compHasToBeChanged = false;
		CalculatePartialRanks partialRanks = new CalculatePartialRanks();
		DisplayRuntimeAndRank display = new DisplayRuntimeAndRank(RTE.getRte_disp_interface(), rte_disp.displayRuntimeAndRankDelayAfterSaving, rte_disp.brightness);
		
		/*
		 * Czekanie 3 sekundy
		 */
		try {
			Thread.sleep(3000, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Sprawdzanie czy użytkownik nie kliknął przez przypadek w jakiejś innej komórce w tabeli
		 */
		if (rte_gui.runtimeFromChrono) {
			LugerCompetitor savedCmptr = rte_st.actuallyOnTrack;
			
			SaveRuntime.saveRuntimeForCurrentCmptr(rt);
//			SaveRuntime.displayRuntimeOnDisplay(rt, rte_st.actuallyOnTrack);
			if (GeneralS.isPartialRanksRunOnly())
				partialRanks.calculatePartialRanksInRun(rte_st.currentCompetition, rte_st.currentRun);
			else
				partialRanks.calculatePartialRanks(rte_st.currentCompetition, rte_st.currentRun);
			rte_gui.updater.updateCurrentCompetition();		// sktualizowanie modelu wyświetlającego aktualną konkurencje
			display.showScoreAfterRun(rt, rte_st.actuallyOnTrack, rte_st.currentCompetition.partialRanks.get(rte_st.actuallyOnTrack));
			
			// autozapis pliku CSV
			if (rte_st.filePath != null) {
			
				Path path = Paths.get(rte_st.filePath + "pliki_csv/");
				
				// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
				if (!Files.exists(path)) {
					// jeżeli nie istnieje to należy go utworzyć
					new File(rte_st.filePath + "pliki_csv/").mkdirs();
				}
				
				String fn = rte_st.competitions.toString() + "___" + rte_st.currentCompetition.toString().replaceAll(" ", "_") +
						"___" + rte_st.currentRun.toString().replaceAll(" ", "_") + "___po_zawodniku_" + savedCmptr.toString().replaceAll(" ", "_").replaceAll("/", "_")
						+ ".csv";
				
				ScoreTableCsvSaver csv = new ScoreTableCsvSaver(rte_st.filePath + "pliki_csv/" + fn);
				try {
					csv.saveTableToFile(rte_gui.model.getColumnNames(), rte_gui.model.getTableData(), rte_gui.model.getTypes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// autozapis pliku XML
			if (rte_st.filename != null && rte_st.filePath != null) {
				String backupDir = rte_st.filePath + "autozapis/";
				Path backupPath = Paths.get(backupDir);
				
				// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
				if (!Files.exists(backupPath)) {
					// jeżeli nie istnieje to należy go utworzyć
					new File(backupDir).mkdirs();
				}
				
				String filenameToSave = backupDir + rte_st.competitions.toString() + "___" + rte_st.currentCompetition.toString().replaceAll(" ", "_") +
						"___" + rte_st.currentRun.toString().replaceAll(" ", "_") + "___po_zawodniku_" + savedCmptr.toString().replaceAll(" ", "_").replaceAll("/", "_")
						+ ".xml";
				
				saver.setFile(new File(filenameToSave));
				try {
					saver.saveToXml(rte_st.competitions);
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (RteIsNullEx e) {
					e.printStackTrace();
				}
				
			}
			
			// przechodzenie do kolejnego ślizgu
			try {
				UpdateCurrentAndNextLuger.moveForwardNormally();		// tu wyświetla na wyświetlaczu LED kolejnego zawodnika
			} catch (EndOfRunEx e) {
				
				if (!inhibitMsg)
					JOptionPane.showMessageDialog(null, "Zakończył się " + rte_st.currentRun.toString());

				try {
					EndOfRun.process();
				} catch (EndOfCompEx e1) {
					compHasToBeChanged = true;
				}
				
			} catch (AppContextUninitializedEx e) {
				e.printStackTrace();
			} catch (StartOrderNotChoosenEx e) {
				e.printStackTrace();
			}
			
			
			if (compHasToBeChanged) {
				/*
				 * Jeżeli dotarliśmy do końca aktualnej konkurencji i należy przeskoczyć na następną
				 */
				try {
					ChangeCompetition.moveToNextCompetition();
				} catch (AppContextUninitializedEx e) {
					;
				} catch (NoMoreCompetitionsEx e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Nie ma już więcej konkurencji do rozegrania.");

				}
			}
			///
		}
		else {
			return;
		}
		
	}
}
