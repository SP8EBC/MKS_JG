package pl.jeleniagora.mks.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.DisplayS;

public class CompManagerAsaveChbxItemListener implements ItemListener {

	/**
	 * Metoda wywoływana w momencie zaznaczenia albo odznaczenia checkboxa "Autozapis czasu ślizgu"
	 */
	@Override
	public void itemStateChanged(ItemEvent arg0) {
		if (arg0.getStateChange() == ItemEvent.SELECTED) {
			ChronometerS.autosave = true;
			DisplayS.setInhibitMessageAtEndOfRun(true);
			
			System.out.println("Autosave enabled");
		}
		else {
			ChronometerS.autosave = false;
			DisplayS.setInhibitMessageAtEndOfRun(true);
			
			System.out.println("Autosave disabled");
		}
		
	}

}
