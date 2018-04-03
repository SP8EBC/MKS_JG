package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Vector;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import pl.jeleniagora.mks.chrono.Chrono;
import pl.jeleniagora.mks.serial.TypesConverters;

class TypesConvertersTest {

	@Test
	void testConvertByteArrayToShortVector() {
		Vector<Short> wanted = new Vector<Short>(Arrays.asList(new Short[]{240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255}));

		byte test[] = new byte[16];

		for (byte i = 0; i < 16; i++) {
			test[i] = (byte) ((byte)-16+i);
		}
		
		Vector<Short> test_s = TypesConverters.convertByteArrayToShortVector(test);
		
		Assert.assertTrue(test_s.equals(wanted));
		
	}

	@Test
	void testConvertByteArrayToShortArray() {
		short wanted[] = {240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};
		
		byte test[] = new byte[16];

		for (byte i = 0; i < 16; i++) {
			test[i] = (byte) ((byte)-16+i);
		}
		
		short test_s[] = TypesConverters.convertByteArrayToShortArray(test);
		
		Assert.assertArrayEquals(wanted, test_s);

	}
	
	@Test
	void testConvertByteArrayToCharArray() {
		char wanted[] = {'A', 'B', 'C', 'D', 'E', 'F'};
		
		byte test[] = {65, 66, 67, 68, 69, 70};
		
		char test_c[] = TypesConverters.convertByteArrayToCharArray(test);
		
		Assert.assertArrayEquals(wanted, test_c);
	}
	
	@Test
	void testConvertByteArrayToCharVector() {
		Vector<Character> wanted = new Vector<Character>(Arrays.asList(new Character[] {'A', 'B', 'C', 'D', 'E', 'F'}));
		
		byte test[] = {65, 66, 67, 68, 69, 70};

		Vector<Character> test_c = TypesConverters.convertByteArrayToCharVector(test);
		
		Assert.assertTrue(test_c.equals(wanted));
	}
	
	@Test
	void testConvertStringToCharacterVector() {
		String test = new String("ABCDxyz");
		
		Vector<Character> wanted = new Vector<Character>();
		
		wanted.add(new Character('A'));
		wanted.add(new Character('B'));
		wanted.add(new Character('C'));
		wanted.add(new Character('D'));
		wanted.add(new Character('x'));
		wanted.add(new Character('y'));
		wanted.add(new Character('z'));

		Vector<Character> test_c = TypesConverters.convertStringToCharacterVector(test);
		
		Assert.assertTrue(test_c.equals(wanted));
	
	}

	

}
