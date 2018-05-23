package pl.jeleniagora.mks.chrono;

import java.time.LocalTime;

import pl.jeleniagora.mks.serial.TypesConverters;

/**
 * Klasaa umożliwia dokonanie konwersji pomiędzy czasem wyrażanym w setkach mikrosekund a typem LocalTime
 * @author mateusz
 *
 */
public class ConvertMicrotime {

	public static LocalTime toLocalTime(Integer micro) {
		
		Integer hrs = micro / (3600 * 10000);
		Integer min = (micro / (60 * 10000)) % 60;
		Integer sec = (micro / 10000) % 60;
		Integer msec = (micro / 10) % 1000;
		
		Integer nano = msec * TypesConverters.nanoToMilisecScaling;
		
		LocalTime ret = LocalTime.of(hrs, min, sec, nano);

		return ret;
	}
	
	public static Integer fromLocalTime(LocalTime lt) {
		return 0;
	}
}
