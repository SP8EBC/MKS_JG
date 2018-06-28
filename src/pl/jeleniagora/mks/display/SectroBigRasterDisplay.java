package pl.jeleniagora.mks.display;

import java.nio.charset.Charset;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.rte.RTE_COM_DISP;

@Component
public class SectroBigRasterDisplay implements TextDisplayInterface {

	@Autowired
	@Qualifier("comDispBean")
	RTE_COM_DISP com;
	
	final static byte FRAME_START = 0x02;
	final static byte FRAME_END = 0x03;
	
	final static byte TEXT_LL = 0x13;
	final static byte CLEAR = 0x17;
	final static byte TEXT_L1 = 0x11;
	final static byte TEXT_L2 = 0x12;
	final static byte TEXT_BIG = 0x14;
	final static byte LF = 0x0A;
	final static byte CR = 0x0D;
	
	final static byte TIME_DISP = 0x15;
	final static byte TEMP_DISP = 0x16;
	final static byte TIME_SET = 0x18;
	final static byte MODE = 0x19;
	final static byte ECHO = 0x1A;
	final static byte BRIGHT = 0x1C;
	
	boolean scrolling;
	boolean centering;
	
	public SectroBigRasterDisplay() {
		scrolling = false;
		centering = false;
	}
	
	@Override
	public int getColumnRes() {
		return 16;
	}

	@Override
	public int getRowRes() {
		return 3;
	}

	@Override
	public void clearDisplay() {
		Vector<Byte> data = new Vector<Byte>();
		data.add(FRAME_START);
		data.add(CLEAR);
		data.add(FRAME_END);
				
//		while(com.activateTx);
		System.out.println("ClearDisplay");
		try {
			com.txBuferSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		com.txBuffer = new Vector<Byte>(data);
		
		com.txBuferSemaphore.release();
		com.activateTx = true;
		
		synchronized(com.syncTransmitEnd) {
			try {
				com.syncTransmitEnd.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("ClearDisplay - end");

	}

	@Override
	public void sendText(String text, int offsetCol, int offsetRow) {
		String lines[] = text.split("\\r?\\n"); // dzielenie wejściowego strina na linie
		
		if (offsetCol > 0) {
			StringBuilder builder = new StringBuilder();
			
			for (int i = 0; i < offsetCol; i++)
				builder.append(" ");
			
			/*
			 * Jeżeli tekst ma byź przesunięty w prawo o kilka kolumn do trzeba dociepać spacji na początek
			 */			
			for (int i = 0; i < 2; i++) { // wyświetlacz ma i tak tylko dwie linie rozdzielczości także można zignorować reszte				
				lines[i] += builder.toString();
				
			}
		}
		
		if (offsetRow > 0) {
			;
		}
		
		for (int i = 0; i < lines.length; i++) {
			
			Vector<Byte> data = new Vector<Byte>();
			data.add(FRAME_START);
			data.add(TEXT_LL);
			data.add((byte)(i+1));
			
			/*
			 * Typ String w Javie przechowuje tekst zakodowany jako UTF-16 (znaki 2 bajtowe), dlatego trzeba go przekodować na coś
			 * co jest zrozumiałe dla wyświetlacza
			 */
			byte[] line = (lines[i].getBytes(Charset.forName("ISO-8859-2")));
			
			for (int j = 0; j < line.length; j++)
				data.add(line[j]);		// dopisywanie pierwszej linijki
			
			data.add(FRAME_END);
			
			// komunikacja po RS
			System.out.println("SendText line: " + (i + 1));
			try {
				com.txBuferSemaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			com.txBuffer = new Vector<Byte>(data);
			
			com.txBuferSemaphore.release();
			com.activateTx = true;
			
			synchronized(com.syncTransmitEnd) {
				try {
					com.syncTransmitEnd.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
/*			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}

	@Override
	public void setBrightness(int level) throws DisplayFunctionNotSupportedEx {
		Vector<Byte> data = new Vector<Byte>();
		data.add(FRAME_START);
		data.add(BRIGHT);
		data.add((byte)level);
		data.add(FRAME_END);
				
		// komunikacja po RS
//		while(com.activateTx);
		System.out.println("setBrightness");
		try {
			com.txBuferSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		com.txBuffer = new Vector<Byte>(data);
		
		com.txBuferSemaphore.release();
		com.activateTx = true;
		
		synchronized(com.syncTransmitEnd) {
			try {
				com.syncTransmitEnd.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("setBrightness - end");
		
	}

	@Override
	public void setScrolling(boolean en) throws DisplayFunctionNotSupportedEx {
		scrolling = en;
		
		byte mode = (byte) 0xFF;		// nieużywane bity muszą być ustawione na jeden
										// dlatego łatwiej będzie zainicjować 0xFF a potem
										// kasować to co powinno być zerem
		
		int centeringBit = ((centering ? 1 : 0) << 1);	// LSB + 1
		int scrollingBit = (scrolling ? 1 : 0);			// LSB
		
		/*
		 * Wyliczanie wartości do ANDowania z wyjściowym bajtem do wysłania do wyświetlacza.
		 * XOR (^) czyli alternatywa rozłączna daje na wyjściu jeden jeżeli na wejściu są dwa
		 * różne bity. Jeżeli są takie same (dwie jedynki albo dwa zera) to daje zero. W praktyce 
		 * XOR może być używany do odwracania stanu bitu na przeciwny. Jeżeli zXORuje się jedynkę
		 * z jedynką to zamieni się na zero. Jeżeli to zero zXORuje się ponownie z jedynką to 
		 * stan odwróci się na jeden
		 */
		byte and = (byte)(0xFF ^ ((1 << 2) | centeringBit | scrollingBit));
		
		mode &= and;	// ANDowanie z wynikiem XORa. Wyzeruje bity ustawione na zero
		
		Vector<Byte> data = new Vector<Byte>();
		data.add(FRAME_START);
		data.add(MODE);
		data.add((byte)mode);
		data.add((byte)mode);
//		data.add((byte)mode);		
		data.add(FRAME_END);
		
		// komunikacja po RS
		System.out.println("setScrolling");
		try {
			com.txBuferSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		com.txBuffer = new Vector<Byte>(data);
		
		com.txBuferSemaphore.release();
		com.activateTx = true;
		
		synchronized(com.syncTransmitEnd) {
			try {
				com.syncTransmitEnd.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("setScrolling - end");
	}

	@Override
	public void setAutoCentering(boolean en) throws DisplayFunctionNotSupportedEx {
		centering = en;
	}

}
