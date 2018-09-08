package pl.jeleniagora.mks.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.jeleniagora.mks.events.AfterWebServiceContact;
import pl.jeleniagora.mks.exceptions.CompetitionDefinitionRenderEx;
import pl.jeleniagora.mks.online.renderers.CompetitionDataRenderer;
import pl.jeleniagora.mks.online.renderers.CompetitionMappingRenderer;
import pl.jeleniagora.mks.online.renderers.CompetitionsDefinitionRenderer;
import pl.jeleniagora.mks.online.renderers.SingleCompetitionDefinitionRenderer;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.online.CompetitionData;
import pl.jeleniagora.mks.types.online.CompetitionsDefinition;
import pl.jeleniagora.mks.types.online.CompetitionsToCompetitionMapping;
import pl.jeleniagora.mks.types.online.SingleCompetitionDefinition;

/**
 * Klasa odpowiedzialna za "konsuowanie" metod POST oferowanych przez Web Serwis REST 
 * do obsługi 
 * @author mateusz
 *
 */
public class WebServicePutConsumer {

	
	AfterWebServiceContact callback;
	
	public WebServicePutConsumer(AfterWebServiceContact cbk) {
		callback = cbk;
	}
	
	/**
	 * Metoda aktualizująca dane o zawodach
	 * @param competitions
	 */
	public void competitionsUpdater(Competitions competitions) {
		@SuppressWarnings("unused")
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/updateComps";
		
		String username = Secret.user, password = Secret.pass;
		CompetitionsDefinitionRenderer rndr = new CompetitionsDefinitionRenderer();
		CompetitionsDefinition defs;
		
		try {
			defs = rndr.render(competitions);
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
				
				HttpEntity<?> entityRequest = new HttpEntity<>(defs, HttpSimpleAuthHeader.create(username, password));
				
				try {
					template.exchange(uri, HttpMethod.PUT, entityRequest, String.class);
					//template.put(uri, defs);
					return;
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
//					if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
//						if (e.getResponseBodyAsString().equals("ALREADY_CREATED")) {
//							JOptionPane.showMessageDialog(null, "Zawody o tej nazwie znajdują się już w systemie online. Baza danych nie została zmodyfikowana");
//							
//						}
//					}
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
		
	}
	
	/**
	 * Metoda dodająca/aktualizująca mappingi pomiędzy zawodami a konkurencjami
	 * @param competitions
	 */
	public void mappingsUpdater(Competitions competitions) {
		@SuppressWarnings("unused")
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/updateMappings";
		
		if (competitions.competitions == null || competitions.competitions.size() == 0) {
			JOptionPane.showMessageDialog(null, "Nie zdefiniowano żadnej konkurencji!");
			return;
		}
		
		CompetitionsToCompetitionMapping mapping = CompetitionMappingRenderer.render(competitions);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		// uruchamianie zapytania do web-serwisu w osobnym wątku żeby nie blokowac GUI
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionMappingsUpdater");
				try {
					template.put(uri, mapping);
					return;
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
//					if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
//						if (e.getResponseBodyAsString().equals("ALREADY_CREATED")) {
//							JOptionPane.showMessageDialog(null, "Zawody o tej nazwie znajdują się już w systemie online. Baza danych nie została zmodyfikowana");
//							
//						}
//					}
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
/*		
		CompetitionsDefinitionRenderer rndr = new CompetitionsDefinitionRenderer();
		CompetitionsDefinition defs = rndr.render(competitions);
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		// uruchamianie zapytania do web-serwisu w osobnym wątku żeby nie blokowac GUI
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionsCreator");
				try {
					template.put(uri, defs);
					return;
				}
				catch(ResourceAccessException e) {
					// wyjątek rzucany jeżeli nie można ustanowić komunikacji z serwerem
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Wystąpił błąd poczas komunikacji z serwerem luge.pl");
				}
				catch(HttpClientErrorException | HttpServerErrorException e) {
//					if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
//						if (e.getResponseBodyAsString().equals("ALREADY_CREATED")) {
//							JOptionPane.showMessageDialog(null, "Zawody o tej nazwie znajdują się już w systemie online. Baza danych nie została zmodyfikowana");
//							
//						}
//					}
					// wyjątek rzucany jeżeli serwer zwrócił jakiś kod błędu
					System.out.println(e.getResponseBodyAsString());
				}
			}
		
		}.run();
		
	*/	
	}
	
	/**
	 * Klient web serwisu wywoływany po każdym użyciu przycisku zapisz czas zawodnika, do aktualizowania 
	 * 
	 * @param competition
	 */
	public void competitionDataUpdater(Competition competition) {
		final String uri = "http://localhost:8080/MKS_JG_ONLINE/updateCmpData/{id}";

		
		//SingleCompetitionDefinitionRenderer rndr = new SingleCompetitionDefinitionRenderer();
		//SingleCompetitionDefinition def = rndr.render(competition);
		CompetitionData data = CompetitionDataRenderer.renderer(competition);
		
		Map <String, String> params = new HashMap<String, String>();
		params.put("id", new Long(competition.serialNum).toString());
		
		List<HttpMessageConverter<?>> list = new ArrayList<HttpMessageConverter<?>>();
		list.add(new MappingJackson2HttpMessageConverter());
		
		RestTemplate template = new RestTemplate();
		
		new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("competitionDataUpdater");
				try {
					template.put(uri, data, params);
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
