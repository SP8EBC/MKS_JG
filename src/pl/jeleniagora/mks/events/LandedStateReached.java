package pl.jeleniagora.mks.events;

import java.time.LocalTime;
import java.util.Vector;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.types.AppContextUninitializedEx;
import pl.jeleniagora.mks.types.EndOfCompetitionEx;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/*
 * Klasa używana w momencie kiedy saneczkarz dojedzie do mety, czyli maszyna stanu pomiaru czasu
 * przeskocy w stan LANDED
 */
public class LandedStateReached {
	
	static AnnotationConfigApplicationContext ctx;

	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void process(LocalTime runtime) throws AppContextUninitializedEx {
		
		if (ctx == null) 
			throw new AppContextUninitializedEx();
		
		LandedStateReached.updateTextFieldsInCM(runtime);
		
		LandedStateReached.saveRunTimeForLuger(runtime);
		try {
			LandedStateReached.setActuallyAndNextOnTrack();
		} catch (EndOfCompetitionEx e) {
			e.printStackTrace();
		}
		
		
	
	}
	
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
	
	static void saveRunTimeForLuger(LocalTime runTime) {
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

		/*
		 * Referencja do zawodnika który aktualnie ukonczył ślizg
		 */
		LugerCompetitor currentToSaveRuntime = rte_st.actuallyOnTrack;
		
		
		Run current = rte_st.currentRun;
		LocalTime runtimeToUpdate = current.run.get(currentToSaveRuntime);
		runtimeToUpdate = runTime;
		
	}
	
	static void setActuallyAndNextOnTrack() throws EndOfCompetitionEx {

	}
	
}
