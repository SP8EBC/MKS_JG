package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.display.DisplayRuntimeAndRank;
import pl.jeleniagora.mks.events.EndOfRun;
import pl.jeleniagora.mks.events.SaveRuntime;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.files.xml.XmlSaver;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_DISP;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.scoring.CalculatePartialRanks;
import pl.jeleniagora.mks.settings.GeneralS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class CompManagerStoreRuntimeBtnActionListener implements ActionListener {

	AnnotationConfigApplicationContext ctx;

	public CompManagerStoreRuntimeBtnActionListener(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		Integer minutes = 0, seconds = 0, milis = 0;
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_DISP rte_disp = (RTE_DISP)ctx.getBean("RTE_DISP");
		XmlSaver saver = (XmlSaver)ctx.getBean(XmlSaver.class);
		
		CalculatePartialRanks partialRanks = new CalculatePartialRanks();
		DisplayRuntimeAndRank display = new DisplayRuntimeAndRank(RTE.getRte_disp_interface(), rte_disp.displayRuntimeAndRankDelayAfterSaving, rte_disp.brightness);
		
		try {
			minutes = new Integer(rte_gui.min.getText());
		}
		catch (Exception e) {
			;
		}
		
		try {
			seconds = new Integer(rte_gui.sec.getText());
		}
		catch (Exception e) {
			;
		}
		
		try {
			milis = new Integer(rte_gui.msec.getText());
		}
		catch (Exception e) {
			;
		}
		
		if (minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59 || milis < 0 || milis > 999) {
			JOptionPane.showMessageDialog(null, "Niepoprawny format czasu!");
			rte_gui.min.setText("0");
			rte_gui.sec.setText("0");
			rte_gui.msec.setText("0");
			return;
		}
		
		if (rte_st.currentCompetition == null) {
			JOptionPane.showMessageDialog(null, "Nie ustawiono aktualnie rozgrywanej konkurencji");
			return;			
		}
		
		if (rte_gui.competitorClickedInTable == null) {
			JOptionPane.showMessageDialog(null, "Należy wybrać saneczkarza dla którego czas ma zostać zapisany");
			return;
		}
		
		Integer nanos = milis * CompManagerScoreTableTimeRenderer.nanoToMilisecScaling;
		LocalTime runTime = LocalTime.of(0, minutes, seconds, nanos);

		System.out.println("Saving runtime: " + runTime.toString() + " for comptr '" + rte_gui.competitorClickedInTable.toString() + "' in run "
				+ rte_gui.runClickedInTable);
		
		boolean ret = SaveRuntime.saveRuntimeForMarkedCmptr(runTime);	// funkcja oblicza również partial ranks i zapisuje CSV
				
		if (ret) {
			LugerCompetitor savedCmptr = rte_st.actuallyOnTrack;	// referencja na saneczkarza któremu czas zapisano
			Run savedRun = rte_st.currentRun;		// referencja na ślizg w którym czas zapisano
			Competition savedCompetition = rte_st.currentCompetition;	// referencja na 
			
			/*
			 * Jeżeli zapisuje się przy użyciu przycisku czas dla zawodnika aktualie na torze to go wyświetl na wyśw LED
			 */
			
			Short partialRank = rte_st.currentCompetition.partialRanks.get(rte_st.actuallyOnTrack);
			
			if (partialRank != null)
				display.showScoreAfterRun(runTime, rte_st.actuallyOnTrack, partialRank);
//			SaveRuntime.displayRuntimeOnDisplay(runTime, rte_st.actuallyOnTrack);		// podtrzymanie domyślnie przez 9 sekund
			
			try {
				UpdateCurrentAndNextLuger.moveForwardNormally();
			} catch (EndOfRunEx e) {
				/*
				 * Jeżeli zapisano czas ślizgu dla ostatniego w konkurencji 
				 */
				JOptionPane.showMessageDialog(null, "Zakończył się " + rte_st.currentRun.toString());
				
				try {
					EndOfRun.process();		// zmiana ślizgu na następny
					
					String msgString = new String();
					if (rte_st.currentRun.trainingOrScored) {
						msgString += "Punktowany ";
					}
					else {
						msgString += "Treningowy ";
					}
					msgString += rte_st.currentRun.numberInScoredOrTrainingRuns + 1;
					
					JOptionPane.showMessageDialog(null, "Aktualny ślizg: " + msgString);
				} catch (EndOfCompEx e1) {
					e1.printStackTrace();
				}
				
			} catch (AppContextUninitializedEx e) {
				e.printStackTrace();
			} catch (StartOrderNotChoosenEx e) {
				e.printStackTrace();
			}
			
			/*
			 * Jeżeli użytownik zapisuje czas dla zawodnika aktualnie na torze nalezy wykonać autozapis
			 */
			if (rte_st.filename != null && rte_st.filePath != null) {
				String backupDir = rte_st.filePath + "autozapis/";
				Path backupPath = Paths.get(backupDir);
				
				// sprawdzanie czy istnieje katalog na kopie zapasowe (autozapis)
				if (!Files.exists(backupPath)) {
					// jeżeli nie istnieje to należy go utworzyć
					new File(backupDir).mkdirs();
				}
				
				String filenameToSave = backupDir + rte_st.competitions.toString() + "___" + savedCompetition.toString().replaceAll(" ", "_") +
						"___" + savedRun.toString().replaceAll(" ", "_") + "___po_zawodniku_" + savedCmptr.toString().replaceAll(" ", "_").replaceAll("/", "_")
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
			
		}
		else {
			;
		}

	}

}
