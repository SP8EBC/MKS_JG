package pl.jeleniagora.mks.types;

/**
 * Klasa pomocnicza do zapisu/odczytu z plików XML służąca do przechowywania aktualnego stanu programu
 * @author mateusz
 *
 */
public class ProgramState {

	public Competition currentCompetition;
	
	public LugerCompetitor actuallyOnTrack;
	
	public LugerCompetitor nextOnTrack;
	
	public LugerCompetitor returnCmptr;
	
	public Run currentRun;
	
	public int currentRunCnt;
}
