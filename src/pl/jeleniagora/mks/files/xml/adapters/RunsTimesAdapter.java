package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.Run;

public class RunsTimesAdapter extends XmlAdapter<RunsTimesAdapter.AdaptedRunsTimes, Vector<Run>> {

	public static class AdaptedRunsTimes {
		public List<AdaptedRun> runTimes;
	}
	
	public static class AdaptedRun {
		public Run run;
		
		public AdaptedRun(Run r) { run = r; }
	}
	
	@Override
	public AdaptedRunsTimes marshal(Vector<Run> arg0) throws Exception {
		AdaptedRunsTimes out = new AdaptedRunsTimes();
		out.runTimes = new ArrayList<AdaptedRun>();
		
		for (Run r : arg0) {
			AdaptedRun a = new AdaptedRun(r);
			out.runTimes.add(a);
		}
		
		
		
		return out;
	}

	@Override
	public Vector<Run> unmarshal(AdaptedRunsTimes arg0) throws Exception {
		return null;
	}

}
