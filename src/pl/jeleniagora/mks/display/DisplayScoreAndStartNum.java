package pl.jeleniagora.mks.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.factories.LugersFactory;
import pl.jeleniagora.mks.types.LugerCompetitor;

public class DisplayScoreAndStartNum implements ActionListener {

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

	@Override
	public void actionPerformed(ActionEvent e) {
		LugerCompetitor demo = LugersFactory.createNewLugerSingleFromName("Mateusz", "Lubecki", false, LocalDate.of(1990, 9, 12), ClubsFactory.createNewFromName("SP9KAT"));
		
		demo.setStartNumber((short) 8);
		
		LocalTime time = ConvertMicrotime.toLocalTime(450000);
		
		this.showScoreAfterRun(time, demo);
	}
	
}
