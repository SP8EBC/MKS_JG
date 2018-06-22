package pl.jeleniagora.mks.files.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competitions;

@Component
public class XmlSaver {

	RTE_ST rte_st;
	
	/**
	 * Nazwa wynikowego pliku XML
	 */
	String fn;
	
	public XmlSaver(String filename) {
		fn = filename;
	}
	
	@Autowired
	void setRTE(RTE_ST st) {
		rte_st = st;
	}
	
	public void saveToXml(Competitions competitions) {
		JAXBContext context = JAXBContext.newInstance(Competitions.class);
		Marshaller mar= context.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(competitions, new File(fn));
	}
}
