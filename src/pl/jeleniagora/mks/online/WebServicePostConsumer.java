package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.online.renderers.CompetitionsDefinitionRenderer;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;

public class WebServicePostConsumer {
	
	public void competitionsCreator(Competitions cmps) {
		
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/addComps";

		CompetitionsDefinitionRenderer rndr = new CompetitionsDefinitionRenderer();
		CompetitionsDefinition defs = rndr.render(cmps);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionsCreator");
				try {
					template.postForLocation(uri, defs);
					return;
				}
				catch(ResourceAccessException e) {
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException e) {
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
	}

}
