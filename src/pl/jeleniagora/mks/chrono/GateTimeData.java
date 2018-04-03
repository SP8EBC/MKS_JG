package pl.jeleniagora.mks.chrono;

import java.time.LocalTime;

public class GateTimeData {
	
	private int gateId;
	private LocalTime time;
	
	public int getGateId() {
		return gateId;
	}
	public void setGateId(int gateId) {
		this.gateId = gateId;
	}
	public LocalTime getTime() {
		return time;
	}
	public void setTime(LocalTime time) {
		this.time = time;
	}
}
