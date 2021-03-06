package pl.jeleniagora.mks.files.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.display.DisplayNameSurnameAndStartNum;
import pl.jeleniagora.mks.display.SectroBigRasterDisplay;
import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE;
import pl.jeleniagora.mks.rte.RTE_DISP;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.ProgramState;
import pl.jeleniagora.mks.types.Reserve;

@Component
public class XmlLoader {

	RTE_ST rte_st;
	RTE_GUI rte_gui;
	RTE_DISP rte_disp;
	
	String filename;
	File file;

	public XmlLoader() {
		filename = null;
		file = null;
	}
	
	@Autowired
	void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	@Autowired
	void setRTEGUI(RTE_GUI gui) {
		rte_gui = gui;
	}
	
	@Autowired
	void setRTEDISP(RTE_DISP disp) {
		rte_disp = disp;
	}
	
	public void setFilename(String fn) {
		filename = fn;
	}
	
	public void setFile(File f) {
		file = f;
	}
	 
	public Competitions loadFromXml(boolean updateLedDisplay) throws JAXBException, RteIsNullEx, UninitializedCompEx, Reserve {
		Competitions competitions;
		File inputFile;
		
		JAXBContext context = JAXBContext.newInstance(Competitions.class);
		Unmarshaller un = context.createUnmarshaller();
		
		if (file != null)
			inputFile = file;
		else
			inputFile = new File(filename);
			
		competitions = (Competitions) un.unmarshal(inputFile);
		restoreProgramState(competitions, competitions.programState);
		restoreUserInterface(competitions);
		if (updateLedDisplay) {
			DisplayNameSurnameAndStartNum disp = new DisplayNameSurnameAndStartNum(RTE.getRte_disp_interface(), rte_disp.brightness);
			disp.showBefore(rte_st.actuallyOnTrack);
			
		}
		
		rte_st.competitions = competitions;
		
		return competitions;
	}
	
	void restoreProgramState(Competitions competitions, ProgramState state) throws RteIsNullEx {
		String str;
		if (rte_st == null || rte_gui == null)
			throw new RteIsNullEx();
		
		if (state._currentCompetition != 0) {
			rte_st.currentCompetition = competitions.competitions.get(state._currentCompetition);
			rte_st.currentRun = competitions.competitions.get(state._currentCompetition).runsTimes.get(state.currentRunCnt);
		}
		else {
			rte_st.currentCompetition = null;
			rte_st.currentRun = null;
		}

		if (state._actuallyOnTrack != 0) {
			rte_st.actuallyOnTrack = competitions.findLugerCompetitorBySystemId(state._actuallyOnTrack);
		}
		else {
			rte_st.actuallyOnTrack = null;
		}
		
		if (state._nextOnTrack != 0)
			rte_st.nextOnTrack = competitions.findLugerCompetitorBySystemId(state._nextOnTrack);
		else {
			rte_st.nextOnTrack = null;
		}
		if (state._returnCmptr != 0) {
			rte_st.returnComptr = competitions.findLugerCompetitorBySystemId(state._returnCmptr);
		}
		rte_st.currentRunCnt = (short) state.currentRunCnt;
				
		if (rte_gui.actuallyOnTrack != null && rte_st.actuallyOnTrack != null)
			rte_gui.actuallyOnTrack.setText(rte_st.actuallyOnTrack.toString());
		else 
			rte_gui.actuallyOnTrack.setText("---");
		
		if (rte_st.nextOnTrack != null) {
			str = rte_st.nextOnTrack.toString();
		}
		else
			str = "---";
		
		rte_gui.nextOnTrack.setText(str);
	}
	
	void restoreUserInterface(Competitions competition) throws UninitializedCompEx, Reserve {
		rte_gui.model.updateTableHeading(rte_st.currentCompetition, false);
		rte_gui.model.updateTableData(rte_st.currentCompetition, false);
		
		rte_gui.model.fireTableStructureChanged();
		if (rte_st.currentCompetition != null)
			rte_gui.currentCompetition.setText(rte_st.currentCompetition.toString());
		else 
			rte_gui.currentCompetition.setText("");
		
		rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
		rte_gui.competitorPreviouslyClicked = rte_st.actuallyOnTrack;
		
		rte_gui.runClickedInTable = rte_st.currentRun;
		rte_gui.runPreviouslyClicked = rte_st.currentRun;

	}
	
}
