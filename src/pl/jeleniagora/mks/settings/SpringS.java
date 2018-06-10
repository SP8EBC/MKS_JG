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
@ComponentScan(basePackages = "pl.jeleniagora.mks.rte")
@ImportResource({"classpath*:luge-tracks-spring-ctx.xml"})
public class SpringS {

}
