package pl.jeleniagora.mks.factories;

import java.util.Vector;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class RunsFactory {

	public static Vector<Run> createNewRunsFromLugersVct(Vector<LugerCompetitor> in, int allRuns, int trainingRuns) {
		
		Vector<Run> runs = new Vector<Run>();
		
		short scored = (short) (allRuns - trainingRuns);
		
		for (int i = 0; i < (trainingRuns); i++ ) {
				Run r = new Run(in, (byte)0);
				runs.add(r);
				
				r.number = (short) (i + trainingRuns);
			}
		
		for (int i = 0; i < (scored); i++ ) {
			Run r = new Run(in, (byte)1);
			runs.add(r);
			
			r.number = (short) (i + trainingRuns);
		}
		
		return runs;
	}
	
	public static Vector<Run> createNewRunsFromLugersVct(Vector<LugerCompetitor> in, int allRuns, int trainingRuns, boolean rand) {
		
		Vector<Run> runs = new Vector<Run>();
		
		short scored = (short) (allRuns - trainingRuns);
		
		for (short i = 0; i < (trainingRuns); i++ ) {
			Run r = new Run(in, true, (byte)0);
			runs.add(r);
			
			r.number = (i);	// ustawianie numeru kolejnego ślizgu w konkurencji, styl programistyczny zaczynając od treningowych
			r.numberInScoredOrTrainingRuns = i;
		}
		
		for (short i = 0; i < (scored); i++ ) {
			
			Run r = new Run(in, true, (byte)1);
			runs.add(r);
			
			r.number = (short) (i + trainingRuns);
			r.numberInScoredOrTrainingRuns = i;
		}
		
		return runs;
	}
 }
