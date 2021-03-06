package pl.jeleniagora.mks.events;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.display.DisplayRuntimeAndStartNum;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_DISP;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.DisplayS;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa używana w momencie kiedy saneczkarz dojedzie do mety, czyli maszyna stanu pomiaru czasu
 * przeskocy w stan LANDED
 */
public class LandedStateReached {
	
	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void process(LocalTime runtime) throws AppContextUninitializedEx {
		
		if (ctx == null) 
			throw new AppContextUninitializedEx();
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_DISP rte_disp = (RTE_DISP)ctx.getBean("RTE_DISP");
		
		LandedStateReached.updateTextFieldsInCM(runtime);
			
		// poprawiono ewentualny null access
		if (rte_st.actuallyOnTrack != null) {
			rte_gui.compManager.markConreteRun(rte_st.actuallyOnTrack.getStartNumber(), rte_st.currentRunCnt);
			
			if (rte_disp.autoShowRuntimeAfterLanding)
				LandedStateReached.displayRuntimeOnDisplay(runtime, rte_st.actuallyOnTrack);
		}
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("mm:ss.SSS");
		rte_gui.lblTimeFromChrono.setText(runtime.format(fmt));
		
		if (ChronometerS.autosave) {
			
			boolean inhibit = DisplayS.isInhibitMessageAtEndOfRun();
			
			rte_gui.runtimeFromChrono = true;
			
			new Thread(new SaveRuntimeDelayThread(ctx, runtime, inhibit)).start();
		

		}
		else {
			;
		}

		
		
	
	}
	
	/**
	 * Prywatana metoda wyświetlająca 
	 * @param runTime
	 */
	static void displayRuntimeOnDisplay(LocalTime runTime, LugerCompetitor whoFinished) {
		RTE_DISP rte_disp = (RTE_DISP)ctx.getBean("RTE_DISP");

		DisplayRuntimeAndStartNum d = new DisplayRuntimeAndStartNum(RTE.getRte_disp_interface(), rte_disp.brightness);
				
		/*
		 * Przetrzymanie treści na wyświetlaczu wynosi 2500 bo po 3 sekundach nastąpi autozapis
		 */
		d.showScoreAfterRunAndDelay(runTime, whoFinished, rte_disp.displayRuntimeDelayAfterLanded);
	}
	
	/**
	 * Prywatna metoda ustawiająca wartości pól tekstowych czasu ślizgu
	 * @param runTime
	 */
	static void updateTextFieldsInCM(LocalTime runTime) {
				
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
	
	@Deprecated
	static void saveRunTimeForLuger(LocalTime runTime) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		/*
		 * Referencja do zawodnika który aktualnie ukonczył ślizg
		 */
		LugerCompetitor currentToSaveRuntime = rte_st.actuallyOnTrack;
		
		
		Run current = rte_st.currentRun;
		LocalTime runtimeToUpdate = current.totalTimes.get(currentToSaveRuntime);	// metoda get zwraca kopię tego co jest zapisane w mapie
		runtimeToUpdate = runTime;
		
		/*
		 * Zapisywanie nowego czasu ślizgu na powrót w mapie
		 */
		current.totalTimes.put(currentToSaveRuntime, runtimeToUpdate);
		
		try {
			/*
			 * Aktualizacja modelu (bezpośredniego źródła danych) dla głównej tabeli w CompManagerze
			 */
			rte_gui.compManagerScoreModel.updateTableData(rte_st.currentCompetition, false);
		} catch (UninitializedCompEx e) {
			// w zasadzie w tym miejscu ten wyjątek nigdy nie zostanie rzucony
			e.printStackTrace();
		}
		
		rte_gui.compManagerScoreModel.fireTableDataChanged();
		
		
		
	}
	
}
