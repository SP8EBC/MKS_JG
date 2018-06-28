package pl.jeleniagora.mks.rte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.display.SectroBigRasterDisplay;

@Component
public class RTE {

	static RTE_ST rte_st;
	
	static RTE_GUI rte_gui;
	
	static RTE_COM rte_com;
	
	static SectroBigRasterDisplay rte_disp_interface;
	
	
	public static SectroBigRasterDisplay getRte_disp_interface() {
		return rte_disp_interface;
	}

	@Autowired
	public void setRte_disp_interface(SectroBigRasterDisplay rte_disp_interface) {
		RTE.rte_disp_interface = rte_disp_interface;
	}

	@Autowired
	public void setST(RTE_ST st) {
		rte_st = st;
	}
	
	public static RTE_ST getST() {
		return rte_st;
	}
	
	@Autowired
	public void setGUI(RTE_GUI gui) {
		rte_gui = gui;
	}
	
	public static RTE_GUI getGUI() {
		return rte_gui;
	}
	
	@Autowired
	@Qualifier("comBean")
	public void setCOM(RTE_COM com) {
		rte_com = com;
	}
	
	public static RTE_COM getCOM() {
		return rte_com;
	}
} 
