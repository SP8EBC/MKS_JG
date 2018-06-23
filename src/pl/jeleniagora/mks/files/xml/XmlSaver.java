package pl.jeleniagora.mks.files.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.ProgramState;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XmlSaver {

	RTE_ST rte_st;
	
	/**
	 * Nazwa wynikowego pliku XML
	 */
	String fn;
	
	public XmlSaver(String filename) {
		fn = filename;
	}
	
	public XmlSaver() {
		
	}
	
	@Autowired
	void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	public void setFilename(String _fn) {
		fn = _fn;
	}
	
	public void saveToXml(Competitions competitions) throws JAXBException, RteIsNullEx {
		if (rte_st == null)
			throw new RteIsNullEx();
		
		ProgramState pgmState = new ProgramState();
		
		pgmState.actuallyOnTrack = rte_st.actuallyOnTrack;
		pgmState.nextOnTrack = rte_st.nextOnTrack;
		pgmState.currentCompetition = rte_st.currentCompetition;
		pgmState.returnCmptr = rte_st.returnComptr;
		pgmState.currentRun = rte_st.currentRun;
		pgmState.currentRunCnt = rte_st.currentRunCnt;
		
		competitions.programState = pgmState;
		
		JAXBContext context = JAXBContext.newInstance(Competitions.class);
		Marshaller mar= context.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(competitions, new File(fn));
	}
}
