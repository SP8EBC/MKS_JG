package pl.jeleniagora.mks.factories;

import java.util.Vector;

import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

public class RunsFactory {

	public static Vector<Run> createNewRunsFromLugersVct(Vector<LugerCompetitor> in, int allRuns, int trainingRuns) {
		
		Vector<Run> runs = new Vector<Run>();
		
		for (int i = 0; i < (allRuns); i++ ) {
			runs.add(new Run(in));
		}
		
		return runs;
	}
 }
