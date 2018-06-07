package pl.jeleniagora.mks.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

public class CompManagerNameAndDateCalndrListener implements PropertyChangeListener {

	CompManagerNameAndDateWindow parentWindow;
	
	CompManagerNameAndDateCalndrListener(CompManagerNameAndDateWindow parent) {
		parentWindow = parent;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
        System.out.println(e.getPropertyName()+ ": " + e.getNewValue());
        
        Object o = e.getNewValue();
        
        if (o instanceof Calendar) {
	        final Calendar c = (Calendar)o;
	        parentWindow.selectedDay = c.get(Calendar.DAY_OF_MONTH);
	        parentWindow.selectedMonth = c.get(Calendar.MONTH) + 1;
	        parentWindow.selectedYear = c.get(Calendar.YEAR);
        }
	}

}
