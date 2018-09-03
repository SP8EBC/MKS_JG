package pl.jeleniagora.mks.display;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.types.LugerCompetitor;

/**
 * Klasa służąca do wyświetlania na wyświetlaczu informacji o zakończonym ślizgu
 * @author mateusz
 *
 */
public class DisplayRuntimeAndRank {
	
	TextDisplayInterface display;
	
	short bright;
		
	int delay = 0;
	
	public DisplayRuntimeAndRank(TextDisplayInterface interf, int delay_msecs, short brightness) {
		display = interf;
		delay = delay_msecs;
		bright = brightness;

	}
	
	public void showScoreAfterRun(LocalTime runtime, LugerCompetitor cmptr, int rank) {
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("m:ss.SSS");
		String timeString = fmt.format(runtime);
		
		String dispString = cmptr.toString() + " \r\n" + timeString + " \r\nMiejsce " + rank;
		
		// wysłanie danych do wyświetlacza idzie w osobnym wątku aby nie blokowac UI
		new Thread(new ShowScoreAndRankAfterRun(dispString, delay)).start();
	}
	
	private class ShowScoreAndRankAfterRun implements Runnable {

		String toDisp;
		
		int dely;
		
		ShowScoreAndRankAfterRun(String toDisplay, int delay) {
			toDisp = toDisplay;
			dely = delay;
		}
		
		@Override
		public void run() {
			display.waitForDisplay();
			display.clearDisplay();
			try {
				display.setScrolling(false);
				display.setBrightness(16);
			} catch (DisplayFunctionNotSupportedEx e) {
				e.printStackTrace();
			}
			display.sendText(toDisp, 0, 0);
			if (dely > 0) {
				try {
					Thread.sleep(dely);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			display.releaseDisplay();
		}
		
	}
}
