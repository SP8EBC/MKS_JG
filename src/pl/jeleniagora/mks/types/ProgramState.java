package pl.jeleniagora.mks.types;

/**
 * Klasa pomocnicza do zapisu/odczytu z plików XML służąca do przechowywania aktualnego stanu programu
 * @author mateusz
 *
 */
public class ProgramState {

	public Competition currentCompetition;
	public int _currentCompetition;
	public long _currentCompetitionSn;
	
	public LugerCompetitor actuallyOnTrack;
	public long _actuallyOnTrack;
	
	public LugerCompetitor nextOnTrack;
	public long _nextOnTrack;
	
	public LugerCompetitor returnCmptr;
	public long _returnCmptr;
	
	public Run currentRun;
	public long _currentRunSn;
	
	public int currentRunCnt;
}
