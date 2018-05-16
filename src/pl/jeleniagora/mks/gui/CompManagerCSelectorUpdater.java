package pl.jeleniagora.mks.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.types.Competition;

/**
 * Klasa służy do aktualizacji zawartości pola rozwijanego służącego do wybierania konkurencji
 * @author mateusz
 *
 */
public class CompManagerCSelectorUpdater {

	AnnotationConfigApplicationContext ctx;

	public CompManagerCSelectorUpdater(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	public void updateSelectorContent(Vector<Competition> in) {
		RTE_GUI rte_gui = ctx.getBean(RTE_GUI.class);
		
		JComboBox<Competition> selector = rte_gui.compManagerCSelector;
		
		selector.removeAllItems();
		for (int i = 0; i < in.size(); i++) {
			selector.addItem(in.get(i));
		}
		selector.setSelectedItem(in.get(0));
		
	}

	
	

}
