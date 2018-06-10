package pl.jeleniagora.mks.files.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import pl.jeleniagora.mks.types.Track;

/**
 * Klasa służąda co zamieniania typu Track (tor) na XML i w drugą stronę
 * @author mateusz
 *
 */
public class TrackAdapter extends XmlAdapter<String, Track> {

	@Override
	public String marshal(Track arg0) throws Exception {
		return null;
	}

	@Override
	public Track unmarshal(String arg0) throws Exception {
		return null;
	}

}
