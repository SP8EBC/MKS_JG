package pl.jeleniagora.mks.events;

import javax.swing.JOptionPane;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.DNF;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class DidNotFinished {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public static void process() {
		try {
			setNotFinishedForCurrentLuger();
		} catch (EndOfRunEx e) {
//			e.printStackTrace(); //
			try {
				RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");

				JOptionPane.showMessageDialog(null, "Zakończył się " + rte_st.currentRun.toString());

				EndOfRun.process();
			} catch (EndOfCompEx e1) {
				e1.printStackTrace();
			}
		} catch (AppContextUninitializedEx e) {
			e.printStackTrace();
		} catch (StartOrderNotChoosenEx e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Wywiłanie metody powoduje ustawienie DNF dla zawodnika aktualnie na torze (bo nic innego nie miało by tu
	 * sensu - tj nie da się zapisać DNF dla kogoś kto już dojechał albo jeszcze nie jechał)
	 * @throws StartOrderNotChoosenEx 
	 * @throws AppContextUninitializedEx 
	 * @throws EndOfRunEx 
	 */
	static void setNotFinishedForCurrentLuger() throws EndOfRunEx, AppContextUninitializedEx, StartOrderNotChoosenEx {
	
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		RTE_CHRONO rte_chrono = (RTE_CHRONO)ctx.getBean("RTE_CHRONO");
		LugerCompetitor l = rte_st.actuallyOnTrack;
		
		if (rte_st.currentRun.trainingOrScored) {
			/*
			 * W ślizgu punktowanym nie dojechanie do mety przez zawodnika dyskwalifikuje go całkowicie 
			 * z dalszej części zawodów
			 */
			for (Run r: rte_st.currentCompetition.runsTimes) {
				if (!r.done) {
					r.run.put(l, DNF.getValue());
					UpdateCurrentAndNextLuger.moveForwardNormally();
					rte_chrono.resetStateMachine = true;		// kasownie maszyny stanów do stanu początkowego
				}
			}
		}
		else {
			/*
			 * Podczas treningu nie ukończenie ślizgu nie ma wpływu na możliwość dalszych startów
			 */
			rte_st.currentRun.run.put(l, DNF.getValue());
			UpdateCurrentAndNextLuger.moveForwardNormally();
			rte_chrono.resetStateMachine = true;		// kasownie maszyny stanów do stanu początkowego

		}
	}
}
