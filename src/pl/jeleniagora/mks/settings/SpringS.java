package pl.jeleniagora.mks.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import pl.jeleniagora.mks.events.AfterWebServiceContact;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.rte.RTE_COM_DISP;

/**
 * 
 * @author mateusz
 *
 */
@Configuration
@ComponentScan(basePackages = "pl.jeleniagora.mks.rte, "
		+ "pl.jeleniagora.mks.types, "
		+ "pl.jeleniagora.mks.files.xml.adapters, "
		+ "pl.jeleniagora.mks.files.xml, "
		+ "pl.jeleniagora.mks.files, "
		+ "pl.jeleniagora.mks.gui,"
		+ "pl.jeleniagora.mks.display,"
		+ "pl.jeleniagora.mks.events,"
		+ "pl.jeleniagora.mks.chrono")
public class SpringS {

	@Bean
	public RTE_COM comBean() {
		return new RTE_COM();
	}
	
	@Bean
	public RTE_COM_DISP comDispBean() {
		return new RTE_COM_DISP();
	}
	
	@Bean
	public AfterWebServiceContact afterWebServiceContact() {
		return new AfterWebServiceContact();
	}
}
