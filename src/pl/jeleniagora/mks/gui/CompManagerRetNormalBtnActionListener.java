package pl.jeleniagora.mks.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;

public class CompManagerRetNormalBtnActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {

		UpdateCurrentAndNextLuger.returnToNormalOrder();
	}

}
