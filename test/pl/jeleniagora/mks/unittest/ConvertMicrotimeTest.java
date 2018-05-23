package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.ConverterRegistry;

import org.junit.Assert;
import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.types.DNS;

class ConvertMicrotimeTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testToLocalTime() {

		int micro = 12340;
		LocalTime lt = ConvertMicrotime.toLocalTime(micro);
		
		LocalTime w = LocalTime.of(0, 0, 1, 234 * TypesConverters.nanoToMilisecScaling);
		
		Assert.assertEquals(w, lt);
		
		///////
		micro = 610000;
		lt = ConvertMicrotime.toLocalTime(micro);
		
		w = LocalTime.of(0, 1, 1, 0 * TypesConverters.nanoToMilisecScaling);
		
		Assert.assertEquals(w, lt);
		///////
		micro = 36600000;	//dns
		
		lt = ConvertMicrotime.toLocalTime(micro);
		w = LocalTime.of(1, 1, 0);
		
		Assert.assertEquals(w, lt);
		Assert.assertEquals(w, DNS.getValue());
	}

	@Test
	void testFromLocalTime() {
	}

}
