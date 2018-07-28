package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.online.renderers.CompetitionsDefinitionRenderer;
import pl.jeleniagora.mks.online.renderers.SingleCompetitionDefinitionRenderer;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.AddSingleCompetitionReturn;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;
import pl.jeleniagora.mks.types.online.SingleCompetitionDefinition;

public class WebServicePostConsumer {
	
	public void competitionAdder(Competition comp) {
		
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/addCmp";
		
		SingleCompetitionDefinitionRenderer rndr = new SingleCompetitionDefinitionRenderer();
		SingleCompetitionDefinition def = rndr.render(comp);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		// uruchamianie zapytania do web-serwisu w osobnym wątku żeby nie blokowac GUI
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionAdder");
				try {
					HttpEntity<SingleCompetitionDefinition> entityRequest = new HttpEntity<>(def);
					
					// metoda zwraca ResponseEntity w którym jest np od razu zwrócony HTTP return code
					ResponseEntity<AddSingleCompetitionReturn> responseStr = template.exchange(uri, HttpMethod.POST, entityRequest, AddSingleCompetitionReturn.class);
					//String reponseStr = template.postForObject(uri, def, String.class);
					return;
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
	}
	
	public void competitionsCreator(Competitions cmps) {
		
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/addComps";

		CompetitionsDefinitionRenderer rndr = new CompetitionsDefinitionRenderer();
		CompetitionsDefinition defs = rndr.render(cmps);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		// uruchamianie zapytania do web-serwisu w osobnym wątku żeby nie blokowac GUI
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionsCreator");
				try {
					String reponseStr = template.postForObject(uri, defs, String.class);
					return;
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
	}

}
