package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Track;

/**
 * Klasa służąda co zamieniania typu Track (tor) na XML i w drugą stronę
 * @author mateusz
 *
 */
public class TrackAdapter extends XmlAdapter<String, Track> {

	@Override
	public String marshal(Track arg0) throws Exception {
		return arg0.name;
	}

	@SuppressWarnings("resource")
	@Override
	public Track unmarshal(String arg0) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("luge-tracks-spring-ctx.xml");
		String nameForContext = arg0.toLowerCase().substring(0, 7).replace(" ", "_");
		Track track = (Track)context.getBean(nameForContext);
		
		return track;
	}

}
