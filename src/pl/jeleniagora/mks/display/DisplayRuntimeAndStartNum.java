package pl.jeleniagora.mks.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.factories.LugersFactory;
import pl.jeleniagora.mks.types.LugerCompetitor;

public class DisplayRuntimeAndStartNum implements ActionListener {

	TextDisplayInterface disp;
	
	short bright;
	
	int delay = 0;
	
	public DisplayRuntimeAndStartNum(TextDisplayInterface interf, short brightness) {
		disp = interf;
		bright = brightness;
	}
	
	public void showScoreAfterRunAndDelay(LocalTime runtime, LugerCompetitor cmptr, int delay) {
		this.delay = delay;
		this.showScoreAfterRun(runtime, cmptr);

	}
	
	public void showScoreAfterRun(LocalTime runtime, LugerCompetitor cmptr) {
		int startNum = cmptr.getStartNumber();
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("m:ss.SSS");
		String timeString = fmt.format(runtime);
		
		String dispString = "Numer " + startNum + "\r\n" + cmptr.toString() + " \r\n" + timeString + " ";
		
		// wysłanie danych do wyświetlacza idzie w osobnym wątku aby nie blokowac UI
		new Thread(new ShowScoreAfterRun(dispString, delay)).start();
		
		
	}
	
	private class ShowScoreAfterRun implements Runnable {

		String toDisp;
		
		int dely;
		
		ShowScoreAfterRun(String toDisplay, int delay) {
			toDisp = toDisplay;
			dely = delay;
		}
		
		@Override
		public void run() {
			disp.waitForDisplay();
			disp.clearDisplay();
			try {
				disp.setScrolling(false);
				disp.setBrightness(16);
			} catch (DisplayFunctionNotSupportedEx e) {
				e.printStackTrace();
			}
			disp.sendText(toDisp, 0, 0);
			if (dely > 0) {
				try {
					Thread.sleep(dely);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			disp.releaseDisplay();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		LugerCompetitor demo = LugersFactory.createNewLugerSingleFromName("Mateusz", "Lubecki", false, LocalDate.of(1990, 9, 12), ClubsFactory.createNewFromName("SP9KAT"));
		
		demo.setStartNumber((short) 8);
		
		LocalTime time = ConvertMicrotime.toLocalTime(952340);
		
		this.showScoreAfterRun(time, demo);
	}
	
}
