package pl.jeleniagora.mks.gui;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Track;

@Component
public class CompManagerWindowChooseTrackListener implements ListSelectionListener {

//	@Autowired
//	RTE_ST rte_st;
	
//	@Autowired
//	RTE_GUI rte_gui;

	@Autowired
	CompManagerWindowChooseTrack chooser;
	
	public DefaultListModel<Track> model;
	
	public CompManagerWindowChooseTrackListener() {
	
	}
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		ListSelectionModel selModel = (ListSelectionModel)arg0.getSource();
		
		int selection = selModel.getMaxSelectionIndex();
		
		Track selected = model.get(selection);
		
		return;
	}

}
