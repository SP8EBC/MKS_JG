package pl.jeleniagora.mks.files;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Luger;

/**
 * Klasa służy do dodawnia saneczkarzy do programu przez wczytaie listy z pliku CSV. Uwaga! Rutyna zawsze pomija pierwszy wiersz
 * pliku CSV wychodząc z założenia, że jest to tytuł z nazwami pól
 * @author mateusz
 *
 */
@Component
public class LugersDefinitionCsvImporter {

	@Autowired
	RTE_ST rte_st;
	
	public void parseFile(String fn) throws FileNotFoundException, IOException  {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		Reader in = new FileReader(fn);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		for (CSVRecord record : records) {
		    String name = record.get(0);
		    String surname = record.get(1);
		    String birthDate = record.get(2);
		    String club = record.get(3);
		    
		    // pomijanie nagłówka
		    if (name.toLowerCase().equals("imię") ||
		    		surname.toLowerCase().equals("nazwisko") ||
		    		birthDate.toLowerCase().equals("data urodzenia") ||
		    		club.toLowerCase().equals("klub")) {
		    	continue;
		    }
		    
		    LocalDate birth;
		    
		    if (birthDate.contains(".")) {
		    	// jeżeli bithDate zawiera kropkę to oznacza że podana tam jest pełna data
		    	birth = LocalDate.parse(birthDate, formatter);
		    }
		    if (birthDate.contains("-")) {
		    	birth = LocalDate.parse(birthDate);
		    }
		    else {
		    	// jeżeli nie ma tam kropki to przyjmij że to na pewno tylko rok urodzenia
		    	Integer y = Integer.decode(birthDate);
		    	
		    	birth  = LocalDate.of(y, 1, 1);
		    }
		    
			Luger l = new Luger();
			l.birthDate = birth;
			l.name = name;
			l.surname = surname;
			l.email = "";
			l.club = ClubsFactory.createNewFromName(club);
			l.hasBeenAdded = false;
		    
			rte_st.competitions.listOfAllLugersInThisCompetitions.add(l);
			
		}
	}
}
