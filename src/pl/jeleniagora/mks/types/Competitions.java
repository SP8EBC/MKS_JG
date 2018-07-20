package pl.jeleniagora.mks.types;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.jeleniagora.mks.files.xml.adapters.ListOfAllCompetitorsAdapter;
import pl.jeleniagora.mks.files.xml.adapters.ListOfAllLugersAdapter;
import pl.jeleniagora.mks.files.xml.adapters.LocalDateAdapter;
import pl.jeleniagora.mks.files.xml.adapters.ProgramStateAdapter;
import pl.jeleniagora.mks.files.xml.adapters.TrackAdapter;

/**
 * Klasa definiująca całościowo zawody. Mała uwaga językowa: Competition -> Konkurecja ; Competitions -> Zawody
 * @author mateusz
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Competitions {
	
	public final long dataStructureVersion = 0x80000002;
	
	/**
	 * Nazwa zawodów
	 */
	@XmlElement(name="name")
	public String name;
	
	public String organizer;

	/**
	 * Data kiedy odbywają się zawody
	 */
	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	@XmlElement(name="date")
	public LocalDate date;

	/**
	 * Tor na którym odbywają się zawody
	 */
	@XmlJavaTypeAdapter(value = TrackAdapter.class)
	@XmlElement(name="trackName")
	public Track track;
	
	/**
	 * Lista przechowywująca referencję do wszystkich pojawiających się w tych zawodach indywdualnych
	 * zawodników, czyli wszystkich egzemplarzy klas Luger.
	 */
	@XmlJavaTypeAdapter(value = ListOfAllLugersAdapter.class)
	@XmlElement(name = "lugers")
	public List<Luger> listOfAllLugersInThisCompetitions;
	
	/**
	 * Lista przechowująca wszystkie referencję do ezgemplarzy typu bazowego LugerCompetitor pojawiające się 
	 * w tych zawodach
	 */
	@XmlJavaTypeAdapter(value= ListOfAllCompetitorsAdapter.class)
	@XmlElement(name = "competitors")
	public List<LugerCompetitor> listOfAllCompetingLugersInThisComps;
	
	/**
	 * Wektor z wszystkimi konkurencjami w tych zawodach
	 */
	@XmlElement(name="competition")
	public Vector<Competition> competitions;
	
	/**
	 * Mapowanie konkurencji do bramki startowej
	 */
	@XmlTransient
	public Map<Competition, StartGate> competitionToStartGateMapping;
	
	/**
	 * Imię i Nazwisko pierwszego sędziego
	 */
	@XmlTransient
	public String Judge1st;
	
	/**
	 * Imię i Nazwisko drugiego sędziego
	 */
	@XmlTransient
	public String Judge2nd;
	
	/**
	 * Imię i Nazwisko trzeciego sędziego
	 */
	@XmlTransient
	public String Judge3rd;
	
	/**
	 * 
	 */
	@XmlElement(name="programState")
	@XmlJavaTypeAdapter(value = ProgramStateAdapter.class)
	public ProgramState programState;
	
	public String toString() {
		DateTimeFormatter fmtr = DateTimeFormatter.ofPattern("YYYY-MM-dd");
		String ds;
		
		if (date != null)
			ds = date.format(fmtr);
		else
			ds = new String("");
		
		if (name != null) {
			int nameLn = name.length();
			if (nameLn > 8) nameLn = 8;
			return name.substring(0, nameLn) + "_" + ds;
		}
		else return new String("");
	}
	
	public Competitions() {
		listOfAllLugersInThisCompetitions = new ArrayList<Luger>();
		listOfAllCompetingLugersInThisComps = new ArrayList<LugerCompetitor>();
	}
	
	public Competitions(String _name, LocalDate _date, String _track_name) {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("luge-tracks-spring-ctx.xml");

		listOfAllLugersInThisCompetitions = new ArrayList<Luger>();
		listOfAllCompetingLugersInThisComps = new ArrayList<LugerCompetitor>();
		
		name = _name;
		date = _date;
		track = (Track)context.getBean(_track_name.toLowerCase().substring(0, 7).replace(" ", "_"));
	}
	
	/**
	 * Metoda przeszukuje liste 'listOfAllLugersInThisCompetitions' w poszukiwaniu saneczkarza o wskazanym
	 * systemId
	 * @param systemId
	 * @return
	 */
	public Luger findLugerInListBySystemId(long systemId) {
		
		for (Luger e : this.listOfAllLugersInThisCompetitions) {
			if (e.getSystemId() == systemId)
				return e;
		}
		return null;
	}
	
	/**
	 * Metoda przeszukuje liste 'listOfAllCompetingLugersInThisComps' w poszukiwaniu obiektu klasy 
	 * 'LugerCompetitor' o wskazanym id systemowym
	 * @param systemId
	 * @return
	 */
	public LugerCompetitor findLugerCompetitorBySystemId(long systemId) {
		
		for (LugerCompetitor e : this.listOfAllCompetingLugersInThisComps) {
			if (e.competitorSystemId == systemId)
				return e;
		}
		return null;
		
	}
	
	/**
	 * Metoda zwraca kolejne wolne id które można przyporządkować do nowo tworzonej konkurencji
	 * @return
	 */
	public int getNextIdForNewCompetition() {
		return competitions.size();
	}
}
