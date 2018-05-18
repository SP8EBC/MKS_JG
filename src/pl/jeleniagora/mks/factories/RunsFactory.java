package pl.jeleniagora.mks.factories;

import java.util.Vector;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class RunsFactory {

	public static Vector<Run> createNewRunsFromLugersVct(Vector<LugerCompetitor> in, int allRuns, int trainingRuns) {
		
		Vector<Run> runs = new Vector<Run>();
		
		short scored = (short) (allRuns - trainingRuns);
		
		for (int i = 0; i < (trainingRuns); i++ ) {
			runs.add(new Run(in, (byte)0));
		}
		
		for (int i = 0; i < (scored); i++ ) {
			runs.add(new Run(in, (byte)1));
		}
		
		return runs;
	}
	
	public static Vector<Run> createNewRunsFromLugersVct(Vector<LugerCompetitor> in, int allRuns, int trainingRuns, boolean rand) {
		
		Vector<Run> runs = new Vector<Run>();
		
		short scored = (short) (allRuns - trainingRuns);
		
		for (int i = 0; i < (trainingRuns); i++ ) {
			runs.add(new Run(in, true, (byte)0));
		}
		
		for (int i = 0; i < (scored); i++ ) {
			runs.add(new Run(in, (byte)1));
		}
		
		return runs;
	}
 }
