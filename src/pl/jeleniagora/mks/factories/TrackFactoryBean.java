package pl.jeleniagora.mks.factories;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.types.Track;

@Component
public class TrackFactoryBean implements FactoryBean<Track>{

	private int id;
	private String name;
	private String location;
	private float lat, lon;
	private byte gateNum;
	private byte curvesTotal;
	private short lnTotal;
	private boolean hasArtificalRefrigeration;
	
	@Override
	public Track getObject() throws Exception {
		Track out = new Track();
		
		out.id = id;
		out.name = name;
		out.location = location;
		out.lat = lat;
		out.lon = lon;
		out.gateNum = gateNum;
		out.curvesTotal = curvesTotal;
		out.lnTotal = lnTotal;
		out.hasArtificalRefrigeration = hasArtificalRefrigeration;
		
		return out;
	}

	@Override
	public Class<?> getObjectType() {
		return Track.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public void setGateNum(byte gateNum) {
		this.gateNum = gateNum;
	}

	public void setCurvesTotal(byte curvesTotal) {
		this.curvesTotal = curvesTotal;
	}

	public void setLnTotal(short lnTotal) {
		this.lnTotal = lnTotal;
	}

	public void setHasArtificalRefrigeration(boolean hasArtificalRefrigeration) {
		this.hasArtificalRefrigeration = hasArtificalRefrigeration;
	}

}
