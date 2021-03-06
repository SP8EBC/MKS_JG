package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.exceptions.CompetitionDefinitionRenderEx;
import pl.jeleniagora.mks.online.renderers.CompetitionsDefinitionRenderer;
import pl.jeleniagora.mks.online.renderers.SingleCompetitionDefinitionRenderer;
import pl.jeleniagora.mks.settings.GeneralS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.AddSingleCompetitionReturn;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;
import pl.jeleniagora.mks.types.online.SingleCompetitionDefinition;

public class WebServicePostConsumer {
	
	/**
	 * Metoda używana do dodawania do systemu pojedynczej konkurencji. Przydatna jeżeli najpierw dodało się
	 * całe zawody do luge.pl a potem dodowało się pojedyczą konkurencję
	 * @param comp
	 */
	public void competitionAdder(Competition comp) {
		
		@SuppressWarnings("unused")
		final String uri = GeneralS.getWebServiceHostAddr() + "/addCmp";
		
		String username = Secret.user, password = Secret.pass;
		
		SingleCompetitionDefinitionRenderer rndr = new SingleCompetitionDefinitionRenderer();
		SingleCompetitionDefinition def = rndr.render(comp);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		@SuppressWarnings("unused")
		RestTemplate template = new RestTemplate();
		
		// uruchamianie zapytania do web-serwisu w osobnym wątku żeby nie blokowac GUI
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionAdder");
				try {
					HttpEntity<?> entityRequest = new HttpEntity<>(def, HttpSimpleAuthHeader.create(username, password));
					
					
					// metoda zwraca ResponseEntity w którym jest np od razu zwrócony HTTP return code
					ResponseEntity<AddSingleCompetitionReturn> responseStr = template.exchange(uri, HttpMethod.POST, entityRequest, AddSingleCompetitionReturn.class);
					
					AddSingleCompetitionReturn result = responseStr.getBody();
					
					// jeżeli zawody zostały już wcześniej dodane 
					if (result.alreadyCreated) {
						JOptionPane.showMessageDialog(null, "Konkurencja ta została już wcześniej dodana do systemu. Baza danych luge.pl nie została zmodyfikowana");
					}
					
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
	
	@Deprecated
	public void competitionsCreator(Competitions cmps) {
		
		final String uri = GeneralS.getWebServiceHostAddr() + "/addComps";

		CompetitionsDefinitionRenderer rndr = new CompetitionsDefinitionRenderer();
		CompetitionsDefinition defs;
		
		try {
			defs = rndr.render(cmps);
		} catch (CompetitionDefinitionRenderEx e1) {
			e1.printStackTrace();
			return;
		}
		
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
					if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
						if (e.getResponseBodyAsString().equals("ALREADY_CREATED")) {
							JOptionPane.showMessageDialog(null, "Zawody o tej nazwie znajdują się już w systemie online. Baza danych nie została zmodyfikowana");
							
						}
					}
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
	}

}
