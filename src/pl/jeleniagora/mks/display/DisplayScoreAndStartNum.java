package pl.jeleniagora.mks.display;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pl.jeleniagora.mks.types.LugerCompetitor;

public class DisplayScoreAndStartNum {

	TextDisplayInterface disp;
	
	public DisplayScoreAndStartNum(TextDisplayInterface interf) {
		disp = interf;
	}
	
	void showScoreAfterRun(LocalTime runtime, LugerCompetitor cmptr) {
		int startNum = cmptr.getStartNumber();
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("m:ss.SSS");
		String timeString = fmt.format(runtime);
		
		String dispString = "Numer " + startNum + "\r\n" + cmptr.toString() + " \r\n" + timeString + " ";
		
		// wysłanie danych do wyświetlacza idzie w osobnym wątku aby nie blokowac UI
		new Thread(new ShowScoreAfterRun(dispString)).start();
		
		
	}
	
	private class ShowScoreAfterRun implements Runnable {

		String toDisp;
		
		ShowScoreAfterRun(String toDisplay) {
			toDisp = toDisplay;
		}
		
		@Override
		public void run() {
			disp.clearDisplay();
			disp.sendText(toDisp, 0, 0);
		}
		
	}
	
}
