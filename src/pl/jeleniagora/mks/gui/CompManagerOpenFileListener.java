package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.RteIsNullEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.files.DialogXmlFileFilter;
import pl.jeleniagora.mks.files.xml.XmlLoader;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.Reserve;

@Component
public class CompManagerOpenFileListener implements ActionListener {

	@Autowired
	XmlLoader loader;
	
	@Autowired
	CompManagerCSelectorUpdater selectorUpdater;
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		int answer = JOptionPane.showConfirmDialog(null, "<html><p align=\"center\">Wczytanie zawodów z pliku XML nadpisze wszystkie dane "
				+ "(konkurencję, czasy ślizgów itp)<br> aktualnie przechowywane w programie."
				+ "Czy chcesz kontynuować??</p></html>", "Pozor!", JOptionPane.YES_NO_OPTION);
		
		if (answer == JOptionPane.YES_OPTION) {
			Competitions competitions = null;
			
			JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new DialogXmlFileFilter());		// filtr wyświetlający katalogi i pliki XML
			fc.setAcceptAllFileFilterUsed(false);				// wyłącznie opcji typ pliku : wszystkie
			int ret = fc.showOpenDialog(null);
			
			if (ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				
				loader.setFile(file);
				try {
					competitions = loader.loadFromXml(true);
				} catch (JAXBException e) {
					e.printStackTrace();
				} catch (RteIsNullEx e) {
					e.printStackTrace();
				} catch (UninitializedCompEx e) {
					e.printStackTrace();
				} catch (Reserve e) {
					e.printStackTrace();
				}
				
				selectorUpdater.updateSelectorContent(competitions.competitions);

			}
			else;
		}
		else;
	}

}
