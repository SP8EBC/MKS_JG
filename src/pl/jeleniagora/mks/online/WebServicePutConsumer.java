package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.online.renderers.SingleCompetitionDefinitionRenderer;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.online.SingleCompetitionDefinition;

/**
 * Klasa odpowiedzialna za "konsuowanie" metod POST oferowanych przez Web Serwis REST 
 * do obsługi 
 * @author mateusz
 *
 */
public class WebServicePutConsumer {

	final String uri = "http://luge.pl:8080/scoringComm/onlineApi/competitionDataUpdater/{id}";
	
	public void competitionDataUpdater(Competition competition) {
		SingleCompetitionDefinitionRenderer rndr = new SingleCompetitionDefinitionRenderer();
		SingleCompetitionDefinition def = rndr.render(competition);
		
		Map <String, String> params = new HashMap<String, String>();
		params.put("id", new Integer(competition.id).toString());
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionDataUpdater");
				template.put(uri, def, params);
			}
		
		}.run();
	}
}
