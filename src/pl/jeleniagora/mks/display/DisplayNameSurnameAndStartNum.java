package pl.jeleniagora.mks.display;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.LugerDouble;
import pl.jeleniagora.mks.types.LugerSingle;

public class DisplayNameSurnameAndStartNum {

	TextDisplayInterface disp;

	public DisplayNameSurnameAndStartNum(TextDisplayInterface interf) {
		disp = interf;
	}
	
	public void showBefore(LugerCompetitor cmptr) {
		String name = " ";	// imię a w przypadku dwójek nazwisko tego co jest na górze
		String surname = " ";	// nazwisko a w przypadku dwójek nazwisko tego co na dole
		
		if (cmptr instanceof LugerSingle) {
			LugerSingle s = (LugerSingle)cmptr;
			
			name =  s.single.name;
			surname = s.single.surname;
		}
		
		if (cmptr instanceof LugerDouble) {
			LugerDouble d = (LugerDouble)cmptr;
			
			name = d.upper.surname;
			surname = d.lower.surname;
		}
		
		String dispStr = "Numer " + cmptr.getStartNumber() + "\r\n" + name + " \r\n" + surname + " ";;
		
		new Thread(new ShowAfterRun(dispStr)).start();
	}
	
	class ShowAfterRun implements Runnable {

		String s;
		
		public ShowAfterRun(String str) {
			s = str;
		}
		
		@Override
		public void run() {
			disp.waitForDisplay();
			disp.clearDisplay();
			try {
				disp.setScrolling(false);
				disp.setBrightness(16);
			} catch (DisplayFunctionNotSupportedEx e) {
				e.printStackTrace();
			}
			disp.sendText(s, 0, 0);
			disp.releaseDisplay();
		}
		
	}
}