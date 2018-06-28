package pl.jeleniagora.mks.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa służy do kasowania zawartości wyświetlacza, czyli robienia tak zeby włączony nic nie pokazywał (był czarny)
 * @author mateusz
 *
 */
public class Clear implements ActionListener {

	TextDisplayInterface disp;
	
	public Clear(TextDisplayInterface interf) {
		disp = interf;
	}
	
	void clearScreen() {
		new Thread(new ClearScreen()).start();
	}
	
	class ClearScreen implements Runnable {

		public ClearScreen() {
			
		}
		
		@Override
		public void run() {
			disp.clearDisplay();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.clearScreen();
	}
}
