package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.ProgramState;

@Component
public class ProgramStateAdapter extends XmlAdapter<ProgramStateAdapter.AdaptedProgramState, ProgramState> {

	static RTE_ST rte_st;
	
	@Autowired
	public void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	public static class AdaptedProgramState {
		@XmlElement(required = true, defaultValue = "0")
		int currentCompetition;
		
		public long actuallyOnTrack;
		
		public long nextOnTrack;
		
		public long returnCmptr;
		
		@XmlElement(required = true, defaultValue = "0")
		int currentRun;
		
		
	}

	@Override
	public AdaptedProgramState marshal(ProgramState arg0) throws Exception {
		AdaptedProgramState out = new AdaptedProgramState();
		
		out.currentCompetition = arg0.currentCompetition.id;
		out.actuallyOnTrack = arg0.actuallyOnTrack.getSystemId();
		if (arg0.nextOnTrack != null)
			out.nextOnTrack = arg0.nextOnTrack.getSystemId();
		else
			out.nextOnTrack = 0;
		if (arg0.returnCmptr != null) {
			out.returnCmptr = arg0.returnCmptr.getSystemId();
		}
		else 
			out.returnCmptr = 0;
		
		return out;
		
	}

	@Override
	public ProgramState unmarshal(AdaptedProgramState arg0) throws Exception {
		if(rte_st == null) {
			throw new RteIsNullEx();
		}
		
		ProgramState out = new ProgramState();
		/*
		out.actuallyOnTrack = rte_st.competitions.findLugerCompetitorBySystemId(arg0.actuallyOnTrack);
		out.nextOnTrack = rte_st.competitions.findLugerCompetitorBySystemId(arg0.nextOnTrack);
		if (arg0.returnCmptr != 0) {
			out.returnCmptr = rte_st.competitions.findLugerCompetitorBySystemId(arg0.returnCmptr);
		}
		out.currentCompetition = rte_st.competitions.competitions.get(arg0.currentCompetition);
		out.currentRun = out.currentCompetition.runsTimes.get(arg0.currentRun);
		out.currentRunCnt = arg0.currentRun;
		*/
		
		out._actuallyOnTrack = arg0.actuallyOnTrack;
		out._currentCompetition = arg0.currentCompetition;
		out._nextOnTrack = arg0.nextOnTrack;
		out._returnCmptr = arg0.returnCmptr;
		out.currentRunCnt = arg0.currentRun;
		
		return out;
		
		
	}

}
