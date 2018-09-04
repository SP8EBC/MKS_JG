package pl.jeleniagora.mks.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;

public class DisplayStartScreen implements ActionListener {

	TextDisplayInterface disp;
	
	short bright;

	public DisplayStartScreen(TextDisplayInterface interf, short brightness) {
		disp = interf;
		bright = brightness;
	}
	
	public void showStartScreen() {
		String text = "MKS_JG\r\nwer 1.00\r\nAutor: Mateusz Lubecki = Bielsko-Biała 2018   ";

		new Thread(new ShowStartScreen(text)).start();
		
	}
	
	private class ShowStartScreen implements Runnable {
		
		String toDisp;
		
		ShowStartScreen(String str) {
			toDisp = str;
		}

		@Override
		public void run() {
			disp.waitForDisplay();
			disp.clearDisplay();
			try {
				disp.setScrolling(false);
				disp.setBrightness(bright);
			} catch (DisplayFunctionNotSupportedEx e) {
				e.printStackTrace();
			}			
			disp.sendText(toDisp, 0, 0);
			disp.releaseDisplay();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.showStartScreen();
	}
}
