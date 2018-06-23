package pl.jeleniagora.mks.files.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.ProgramState;
import pl.jeleniagora.mks.types.Reserve;

@Component
public class XmlLoader {

	RTE_ST rte_st;
	RTE_GUI rte_gui;
	
	String filename;

	public XmlLoader() {
		
	}
	
	@Autowired
	void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	@Autowired
	void setRTEGUI(RTE_GUI gui) {
		rte_gui = gui;
	}
	
	public void setFilename(String fn) {
		filename = fn;
	}
	 
	public Competitions loadFromXml() throws JAXBException, RteIsNullEx, UninitializedCompEx, Reserve {
		Competitions competitions;
		
		JAXBContext context = JAXBContext.newInstance(Competitions.class);
		Unmarshaller un = context.createUnmarshaller();
		competitions = (Competitions) un.unmarshal(new File(filename));
		
		restoreProgramState(competitions, competitions.programState);
		restoreUserInterface(competitions);
		
		return competitions;
	}
	
	void restoreProgramState(Competitions competitions, ProgramState state) throws RteIsNullEx {
		if (rte_st == null || rte_gui == null)
			throw new RteIsNullEx();
		
		rte_st.currentCompetition = competitions.competitions.get(state._currentCompetition);
		rte_st.currentRun = competitions.competitions.get(state._currentCompetition).runsTimes.get(state.currentRunCnt);
		rte_st.actuallyOnTrack = competitions.findLugerCompetitorBySystemId(state._actuallyOnTrack);
		rte_st.nextOnTrack = competitions.findLugerCompetitorBySystemId(state._nextOnTrack);
		if (state._returnCmptr != 0) {
			rte_st.returnComptr = competitions.findLugerCompetitorBySystemId(state._returnCmptr);
		}
		rte_st.currentRunCnt = (short) state.currentRunCnt;
				
		rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		rte_gui.nextOnTrack.setText(rte_st.nextOnTrack.toString());
	}
	
	void restoreUserInterface(Competitions competition) throws UninitializedCompEx, Reserve {
		rte_gui.model.updateTableHeading(rte_st.currentCompetition, false);
		rte_gui.model.updateTableData(rte_st.currentCompetition, false);
		
		rte_gui.model.fireTableStructureChanged();

	}
	
}
