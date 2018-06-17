package pl.jeleniagora.mks.rte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RTE {

	static RTE_ST rte_st;
	
	static RTE_GUI rte_gui;
	
	static RTE_COM rte_com;
	
	@Autowired
	public void setST(RTE_ST st) {
		rte_st = st;
	}
	
	@Autowired
	public void setGUI(RTE_GUI gui) {
		rte_gui = gui;
	}
	
	@Autowired
	public void setCOM(RTE_COM com) {
		rte_com = com;
	}
} 
