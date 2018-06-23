package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.files.DialogXmlFileFilter;
import pl.jeleniagora.mks.files.xml.XmlSaver;
import pl.jeleniagora.mks.rte.RTE_ST;

@Component
public class CompManagerSaveFileAsListener implements ActionListener {

	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	XmlSaver saver;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new DialogXmlFileFilter());		// filtr wyświetlający katalogi i pliki XML
		fc.setAcceptAllFileFilterUsed(false);				// wyłącznie opcji typ pliku : wszystkie
		int ret = fc.showSaveDialog(null);
		
		if (ret == JFileChooser.APPROVE_OPTION) {
			saver.setFile(fc.getSelectedFile());
			try {
				saver.saveToXml(rte_st.competitions);
			} catch (JAXBException e) {
				e.printStackTrace();
			} catch (RteIsNullEx e) {
				e.printStackTrace();
			}
		}
		else {
			;
		}
		
		
	}

}
