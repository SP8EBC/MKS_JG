package pl.jeleniagora.mks.files;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import pl.jeleniagora.mks.gui.CompManagerScoreTableTimeRenderer;

public class ScoreTableCsvSaver {
	
	String filename;
	
	public ScoreTableCsvSaver(String fn) {
		filename = fn;
	}

	public void saveTableToFile(String[] columnsTitles, Object[][] data, @SuppressWarnings("rawtypes") Class[] columnsTypes) throws IOException {
		
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));
		
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL
                .withHeader(columnsTitles));
        
        int recCount = data.length;
        int columnCount = columnsTitles.length;
        
        for (int i = 0; i < recCount; i++) {
        	String record[] = new String[columnCount];
        	
        	for (int j = 0; j < columnCount; j++) {
        		if (data[i][j] instanceof Integer) {
        			/*
        			 * Jeżeli wartość w tabeli jest typu Integer to oznacza, że to jest czas ślizgu zapisywany jako 
        			 * krotność setek mikrosekund 
        			 */
        			Integer val = (Integer)data[i][j];
        			record[j] = CompManagerScoreTableTimeRenderer.prepareString(val, true);
        		}
        		else {
        			record[j] = data[i][j].toString();
        		}
        	}
        	
        	csvPrinter.printRecord((Object[])record);
        }
        
        csvPrinter.flush();
        csvPrinter.close();
	}
}
