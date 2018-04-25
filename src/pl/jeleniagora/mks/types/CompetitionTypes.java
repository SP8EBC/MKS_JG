package pl.jeleniagora.mks.types;

public enum CompetitionTypes {
	MEN_SINGLE("Jedynki męskie"),
	WOMAN_SINGLE("Jedynki kobiecie"),
	DOUBLE("Dwójki"),
	DOUBLE_MEN_ONLY("Dwójki męskie"),
	DOUBLE_WOMAN_ONLY("Dwójki kobiecie"),
	DOUBLE_MIXED("Dwójki mieszane"),
	TEAM_RELAY("Sztafeta"),
	TRAINING("Trening"),
	UNINITIALIZED_COMP("");

	/**
	 * Prywatne pole przechowywujące nazwę do wyświetlenia
	 */
	private String name;
	
	private CompetitionTypes(String n) {
		name = n;
	}
	
	/**
	 * Przeciążona metoda zamieniająca na stringa
	 */
	public String toString() {
		return name;
	}
	
}
