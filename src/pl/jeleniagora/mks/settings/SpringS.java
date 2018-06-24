package pl.jeleniagora.mks.settings;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
		+ "pl.jeleniagora.mks.gui,"
		+ "pl.jeleniagora.mls.display")
public class SpringS {

}
