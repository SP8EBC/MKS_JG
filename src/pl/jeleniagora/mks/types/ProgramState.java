package pl.jeleniagora.mks.types;

/**
 * Klasa pomocnicza do zapisu/odczytu z plików XML służąca do przechowywania aktualnego stanu programu
 * @author mateusz
 *
 */
public class ProgramState {

	Competition currentCompetition;
	
	LugerCompetitor actuallyOnTrack;
	
	LugerCompetitor nextOnTrack;
}
