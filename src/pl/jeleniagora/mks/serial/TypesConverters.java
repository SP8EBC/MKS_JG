package pl.jeleniagora.mks.serial;

import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Pomocnicza klasa zawierająca metody służące do konwersji pomiędzy różnymi typami. Przede wszystkim są tu metody
 * konwertujące z tablicy byte na różne inne rzeczy. Ponieważ wadą Javy jest to, że nie ma kompletnie typów bez znaku
 * to pewnym problemem jest właściwe trzymanie danych odebranych z portu szeregowego. Metoda InputStream.read() wymaga
 * na wejściu podania referencji do tablicy prymitywów byte, a ponieważ nie ma do dyspozycji uint8_t aka unsigned char, 
 * to może pojawić się problem jeżeli dane binarne muszą być interpretowane w zakresie 0 do 255. Rozwiązaniem jest 
 * konwersja na wekor albo tablice typów Short.
 * @author mateusz
 *
 */
public class TypesConverters {

	/**
	 * Konwertuje tablice prymitywnych typów byte na wektor Short
	 * @param in
	 * @return
	 */
	public static Vector<Short> convertByteArrayToShortVector(byte[] in) {
		int arrayLn = in.length;
		
		if (arrayLn == 0)
			return null;
		
		Vector<Short> out = new Vector<Short>();
		
		for (int i = 0; i < arrayLn; i++) {
			short im = (short) ((in[i]) & 0xFF);
			
			out.add(im);
		}
		
		return out;
		
	}
	
	/**
	 * Konwertuje tablice prymitywnych typów byte na tablice prymitywnych typów short
	 * @param in
	 * @return
	 */
	
	public static short[] convertByteArrayToShortArray(byte[] in) {
		int arrayLn = in.length;
		
		if (arrayLn == 0)
			return null;
		
		short out[] = new short[arrayLn];
		
		for (int i = 0; i < arrayLn; i++) {
			short im = (short) ((in[i]) & 0xFF);
			
			out[i] = im;
		}
		
		return out;
		
	}
	
	public static char[] convertByteArrayToCharArray(byte[] in ) {
		int arrayLn = in.length;
		
		if (arrayLn == 0)
			return null;
		
		char out[] = new char[arrayLn];

		for (int i = 0; i < arrayLn; i++) {
			char c = (char)(in[i] & 0xFF);
			
			out[i] = c;
		}

		
		
		return out;
	}
	
	public static Vector<Character> convertByteArrayToCharVector(byte[] in) {
		int arrayLn = in.length;
		
		if (arrayLn == 0)
			return null;
		
		//char out[] = new char[arrayLn];
		Vector<Character> out = new Vector<Character>();
		
		for (int i = 0; i < arrayLn; i++) {
			char c = (char)(in[i] & 0xFF);
			
			out.add(c);
		}

		
		
		return out;
	}
	
	/**
	 * Ta metoda jest niezbędna aby przekonwertować wektor egzemplarzy klasy Character
	 * na tablicę prymitywów char. Niestety wektor nie pozwala na "opakowywanie" typów
	 * prymitywnych, a posiadanie stringa w postaci tabblicy char[] jest często konieczne.
	 * @param in
	 * @return
	 */
	public static char[] convertCharacterVectorToCharArray(Vector<Character> in) {
		
		int i = 0;
		
		/*
		 * Tworzenie wyjściowego wektora 
		 */
		char[] out = new char[in.size()];
		
		for (Character c : in) {
			out[i++] = c.charValue();
		}
		
		return out;
	}
	
	public static Vector<Character> convertStringToCharacterVector(String in) {
		
		char[] i = in.toCharArray();
		
		/*
		 * Ta nieco dziwaczna konstrukcja najpier skonwertuje tablicę prymitywów char[] do
		 * tablicy obiektów klas Character[], a następnie przekonwertuje ją na listę. 
		 * Lista jest używana następnie jako argument jednego z konstruktorów wektora,
		 * gdyż Lista implementuje superinterfejs Collection<E>
		 */
		Vector<Character> out = new Vector<Character>(Arrays.asList(ArrayUtils.toObject(i)));
		
		return out;
	}
}
