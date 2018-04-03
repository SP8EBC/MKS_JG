package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ByteToShortTest {

	@Test
	void test() {
		byte test[] = new byte[16];
		short test_s[] = new short[16];
		
		short out[] = {240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255};
		
		for (byte i = 0; i < 16; i++) {
			test[i] = (byte) ((byte)-16+i);
		}
				
		for (int i = 0; i < 16; i++) {
			test_s[i] = (short) ((test[i]) & 0xFF);
		}
		
		byte rn = 13; // \r
		char c = (char)(rn & 0xFF);
		
		if (c == '\r') {
			Assert.assertTrue(true);
		}
		else {
			fail("newline failed");
		}
		Assert.assertArrayEquals(out, test_s);
		//fail("Not yet implemented");
	}

}
