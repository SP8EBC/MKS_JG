package pl.jeleniagora.mks.display;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;

/**
 * Interfejs dla wyświetlacza wyłącznie graficznego, tj. takiego który nie daje możliwości wyświetlania grafiki czy 
 * adresowania pojedynczych pikseli
 * @author mateusz
 *
 */
public interface TextDisplayInterface {

	
	/**
	 * Metoda ma zwracać ilość kolumn tekstu możliwą do wyświetlania na wyświetlaczu
	 * @return
	 */
	public int getColumnRes();
	
	/**
	 * Metoda ma zwracać ilość wierszy tekstu możliwą do wyświetlania
	 * @return
	 */
	public int getRowRes();
	
	/**
	 * Metoda czyszcząca wyświetlacz
	 * @return
	 */
	public void clearDisplay();
	
	/**
	 * Metoda wyświetlająca zadany tekst na wyświetlaczu, metoda powinna rozpoznawać znaczniki końca linii \r\n
	 * i automatycznie dzielić tekst na wiersze
	 * @param text Tekst do wyświetlenia 
	 * @param offsetCol Opcjonalne przesunięcie tekstu o wskazaną ilość kolumn w prawo 
	 * @param offsetRow Opcjonalne przesunięcie tekstu o wskazaną ilość wierszy w dół
	 * @return
	 */
	public void sendText(String text, int offsetCol, int offsetRow);
	
	/**
	 * Metoda ustawiająca jasność wyświetlacza.
	 * @param level Porządana jasność
	 * @throws DisplayFunctionNotSupportedEx Rzucany jeżeli wyświetlacz nie ma takiej funkcji
	 */
	public void setBrightness(byte level) throws DisplayFunctionNotSupportedEx;
	
}
