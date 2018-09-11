package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.files.DialogCsvFileFilter;
import pl.jeleniagora.mks.files.LugersDefinitionCsvImporter;
import pl.jeleniagora.mks.rte.RTE_ST;

@Component
public class CompManagerImportCsvFileListener implements ActionListener {

	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	LugersDefinitionCsvImporter importer;

	@Override
	public void actionPerformed(ActionEvent arg0) {
	
		JOptionPane.showMessageDialog(null, "<html><p align=\"center\">Importowany plik CSV musi zawierać cztery kolumny z odpowiednio: Imieniem, Nazwiskiem, klubem, datą/rokiem urodzenia<br><br></p>"
				+ "<p align = \"center\">Data urodzenia powinna być zapisana w formacie dzien.miesiąc.rok albo dzień-miesiąc-rok, lub sam rok (cztery cyfry)</p>"
				+ "<p align = \"center\">Jeżeli kolumna rok będzie pusta, lub będzie zawierała dane nienumeryczne program automatycznie przyjmie 12 września 1990 jako datę urodzenia</p>"
				+ "<p align = \"center\">Plik powinien być zapisany w formacie Excel CSV z przecinkiem jako separatorem kolumn</p></html>");
		
		
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new DialogCsvFileFilter());		
		fc.setAcceptAllFileFilterUsed(false);				// wyłącznie opcji typ pliku : wszystkie
		int ret = fc.showOpenDialog(null);
		
	
		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			String filePath = fc.getSelectedFile().getPath();
			String fileNameStr = fc.getSelectedFile().getName();
			
			try {
				importer.parseFile(filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else;
		
		return;
	}
	
	
}
