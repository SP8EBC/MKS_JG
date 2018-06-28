package pl.jeleniagora.mks.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayStartScreen implements ActionListener {

	
	TextDisplayInterface disp;

	public DisplayStartScreen(TextDisplayInterface interf) {
		disp = interf;
	}
	
	void showStartScreen() {
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
			disp.clearDisplay();
			disp.sendText(toDisp, 0, 0);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.showStartScreen();
	}
}
