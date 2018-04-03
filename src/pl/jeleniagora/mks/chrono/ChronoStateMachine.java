package pl.jeleniagora.mks.chrono;

/**
 * Enum definiujący maszynę stanów pomiaru czasu ślizgu. W zależności od dokładnej
 * konfiguracji pomiaru czasu (fotoceli) niektóre stany mogą być przeskakiwane
 * @author mateusz
 *
 */
public enum ChronoStateMachine {
	IDLE,
	LINE_UP,			// Saneczkarz na starcie - czerwone światło
	CLEAR_FOR_TAKEOFF,	// Saneczkarz na starcie - zielone światło
	V1_ROTATE,			// Saneczkarz po starcie z uchwytów, wiosłowanie, za pierwszą fotocelą (uruchamianie pomiaru
	AIRBORNE,			// Saneczkarz po starcie, po wiosłowaniu, za drugą fotocelą (pomiar czasu startu)
	EN_ROUTE,			// Za pierwszą fotocelą do czasu pośredniego - w sumie za trzecią
	ON_FINAL,			// Za drugą fotocelą do pomiaru pośredniego - w sumie za czwartą
	LANDED,				// Meta
	
}
