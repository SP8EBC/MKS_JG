package pl.jeleniagora.mks.chrono;

import java.util.Vector;

import pl.jeleniagora.mks.types.Reserve;

public interface Parser {

	public GateTimeData parseFromSerialData(Vector in) throws Reserve;
	
}
