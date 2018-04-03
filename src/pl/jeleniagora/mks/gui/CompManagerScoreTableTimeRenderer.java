package pl.jeleniagora.mks.gui;

import java.awt.Component;
import java.time.Duration;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import pl.jeleniagora.mks.settings.DisplayS;

/**
 * Klasa używana do renderowania czasu na liście z wynikami. Ponieważ czas ślizgu jest zapisywany jako wielokrotność 
 * setek mikrosekund trzymana w typie Integer. Musi to być przeliczane na minuty:sekundy.milisekundy i zwracane jako string 
 * @author mateusz
 *
 */
public class CompManagerScoreTableTimeRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4997477878004315506L;
	
	/**
	 * Stała używana o konwersji pomiędzy nanosekundami a milisekundami
	 */
	public static int nanoToMilisecScaling = 1000000;
	
	CompManagerScoreTableTimeRenderer() {
		super();
	}
	
	/**
	 * Statyczna metoda służąca do generowania Stringa przedstawiającego czas ślizgu na podstawie wejściowego Integera
	 * przedstawiającego czas ślizgu jako całkowitą wielokrotność setek mikrosekund. Wydzielone to zostało z dziedziczonej
	 * metody setValue żeby łatwo napisać do tego testy jednostkowe w JUnit.
	 * @param value
	 * @param digits
	 * @return
	 */
	public static String prepareString(Integer value, boolean digits) {
		
		String timeString = " ";
		
		/* W tabeli wyników czas jest wyświetlany tylko z rozdzielczością 1 milisekundy*/
		Integer timeInMilis = value / 10;
		
		/* obiekt klasy Duration do konwersji */
		Duration time = Duration.ofMillis(timeInMilis);
		
		/* Zapisywanie do osobnych zmiennych liczby minut, sekund i milisekund */
		Long minutes = time.getSeconds() / 60;
		Long seconds = time.getSeconds() % 60;
		Integer miliseconds = (time.getNano() / nanoToMilisecScaling) % 1000;
		
		if (time.getSeconds() > 59) {
			if (digits) {
				/* String zawierający czas przejazdu wyciągnięty z wejściowego Intiger */
				timeString = String.format("%d:%02d.%03d", minutes, seconds, miliseconds);
			}
			else {
				/* Pisanie milisekund bez zer wiodących*/
				timeString = String.format("%d:%02d.%d", minutes, seconds, miliseconds);
			}
		}
		else {
			if (digits) {
				/* String zawierający czas przejazdu wyciągnięty z wejściowego Intiger */
				timeString = String.format("%02d.%03d", seconds, miliseconds);
			}
			else {
				timeString = String.format("%02d.%d", seconds, miliseconds);
			}				
		}
		
		return timeString;
	}
	
	@Override
	public void setValue(Object value) {
				
		/* lokalna zmienna do przechowywania czasu w postaci tekstowej*/
		String timeString = "";
		
		/* Wyciąganie z konfiguracji jak mają być wyświetlane milisekundy */
		boolean digits = DisplayS.isShowAllTimeDigits();
		
		/* Czas jest tu przechowywany jako Integer */
		if (value instanceof Integer) {
			
			Integer v = (Integer)value;
			timeString = CompManagerScoreTableTimeRenderer.prepareString(v, digits);

			setText(timeString);
		}
		
	}

}