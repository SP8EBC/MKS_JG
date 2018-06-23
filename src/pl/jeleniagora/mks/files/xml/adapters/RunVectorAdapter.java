package pl.jeleniagora.mks.files.xml.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Run;

public class RunVectorAdapter extends XmlAdapter<RunVectorAdapter.AdaptedVector, Vector<Run>> {

	public static class AdaptedVector {
		public AdaptedRun runsData;
	}
	
	public static class AdaptedRun {
		public List<Run> run;
		
		public AdaptedRun() { run = new ArrayList<Run>(); }
	}
	
	@Override
	public AdaptedVector marshal(Vector<Run> arg0) throws Exception {
		AdaptedVector out = new AdaptedVector();
		out.runsData = new AdaptedRun();
		
		for (Run r : arg0) {
			out.runsData.run.add(r);
//			out.runTimes.add(a);
		}
		
		
		
		return out;
	}

	@Override
	public Vector<Run> unmarshal(AdaptedVector arg0) throws Exception {
		Vector<Run> out = new Vector<Run>();
		
		for (Run e : arg0.runsData.run) {
			out.add(e);
		}
				
		return out;
	}

}
