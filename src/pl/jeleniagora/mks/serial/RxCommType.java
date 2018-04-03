package pl.jeleniagora.mks.serial;

/**
 * Enum określa jak ma przebiegać odbiór. Czy dane mają być odbierane aż do znacznika końca linii (tryb ASCII aka terminalowy),
 * czy też ma być odebrana określona liczba bajtów bez względu na ich treść. Dostępny jest też tryb z bajtem startowym i końcowym.
 * Wtedy wątek odbiorczy zacznie ciepać dane do bufora dopiero w momencie odbioru przez niego bajtu starty i będzie to robił albo
 * do odbiory bajtu końcowego albo do zapełnienia bufora.
 * @author mateusz
 *
 */
public enum RxCommType {
	COMM_TYPE_UNINIT,
	END_OF_LINE,
	NUM_OF_BYTES,
	START_STOP_BYTES
}
