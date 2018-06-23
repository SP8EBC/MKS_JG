package pl.jeleniagora.mks.settings;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

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
		+ "pl.jeleniagora.mks.gui")
public class SpringS {

}
