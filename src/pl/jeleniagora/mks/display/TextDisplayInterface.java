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
	 * Metoda kasująca zawartość wyświetlacza, bo jej wywołaniu wyświetlacz powinien nic nie wyświetlać
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
	 * Metoda ustawiająca jasność wyświetlacza. Przyjmuje się regulację w zakresie 0 (ciemno) do 255 (jasno)
	 * @param level Porządana jasność
	 * @throws DisplayFunctionNotSupportedEx Rzucany jeżeli wyświetlacz nie ma takiej funkcji
	 */
	public void setBrightness(int level) throws DisplayFunctionNotSupportedEx;
	
	/**
	 * Metoda włączająca bądź wyłączająca funkcję skrolowania wierszy wyświetlacza jeżeli tekst
	 * jest za długi aby zmieścić się w całości
	 * @param en
	 * @throws DisplayFunctionNotSupportedEx
	 */
	public void setScrolling(boolean en) throws DisplayFunctionNotSupportedEx;
	
	/**
	 * Metoda wyłączająca bądź wyłączająca centrowanie tekstu. Przyjmuje się, że wyłączenie tej opcji 
	 * spowoduje że tekst będzie wyrównany do lewej krawędzi ekranu
	 * @param en
	 */
	public void setAutoCentering(boolean en) throws DisplayFunctionNotSupportedEx;
	
}
