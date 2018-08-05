package pl.jeleniagora.mks.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;


/**
 * Pomocniczna klasa służaca do automatycznej aktualizacji i odświeżania modelu
 * @author mateusz
 *
 */
@Component
public class CompManagerUpdateAndRefreshScoreTable {

	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	RTE_GUI rte_gui;
	
	public void updateCurrentCompetition() {
		try {
			rte_gui.compManagerScoreModel.updateTableData(rte_st.currentCompetition, false);
		} catch (UninitializedCompEx e) {
			e.printStackTrace();
		}
		
		rte_gui.compManagerScoreModel.fireTableDataChanged();
	}
}
