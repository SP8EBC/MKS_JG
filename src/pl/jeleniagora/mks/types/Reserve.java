package pl.jeleniagora.mks.types;

/**
 * Główna klasa do obsługi wyjątków 
 * @author mateusz
 *
 */

public class Reserve extends Throwable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1834025274746507099L;

	public enum Type {
		NULL_REFERENCE,
		UNSUFFICIENT_SIZE_OF_TABLE,
		CORRUPTED_INPUT_DATA,
	}
	
	public Reserve(Type t) {
		type = t;
	}
	
	public Type type;
	
}
