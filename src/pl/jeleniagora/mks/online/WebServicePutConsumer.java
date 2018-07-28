package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.events.AfterWebServiceContact;
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

	final String uri = "http://localhost:8080/MKS_JG_ONLINE/competitionDataUpdater/{id}";
	
	AfterWebServiceContact callback;
	
	public WebServicePutConsumer(AfterWebServiceContact cbk) {
		callback = cbk;
	}
	
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
				try {
					template.put(uri, def, params);
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					if (callback != null) {
						callback.noCommWithServer();
					}
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
					if (callback != null) {
						callback.errorStatusReturned(e.getStatusCode());
					}
				}
			}
		
		}.run();
	}
}