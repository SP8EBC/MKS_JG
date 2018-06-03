package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.Assert;

import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.factories.LugersFactory;
import pl.jeleniagora.mks.scoring.CalculateRanksAfterRun;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

class CalculateRanksAfterRunTest {

	HashMap<LugerCompetitor, LocalTime> test;
	
	LugerCompetitor l1, l2, l3, l4, l5, l6, l7;
	LocalTime t1, t2, t3, t4, t5, t6, t7;
	
	@BeforeEach
	void setUp() throws Exception {
		test = new HashMap<LugerCompetitor, LocalTime>();

		LocalDate b = LocalDate.of(1990, 9, 12);
		
		l1 = LugersFactory.createNewLugerSingleFromName("Im", "Naz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l2 = LugersFactory.createNewLugerSingleFromName("Imi", "Nąz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l3 = LugersFactory.createNewLugerSingleFromName("Aąćż", "N", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l4 = LugersFactory.createNewLugerSingleFromName("ěřžšá", "Nazw", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l5 = LugersFactory.createNewLugerSingleFromName("Cazw", "Naz", false, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l6 = LugersFactory.createNewLugerSingleFromName("baa", "Nk", true, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));
		l7 = LugersFactory.createNewLugerSingleFromName("daa", "kz", true, b, ClubsFactory.createNewFromName("MKS Karkonosze Sporty Zimowe"));


	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCalculateTotalRuntime() {
//		fail("Not yet implemented");
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		
		Vector<LugerCompetitor> cmptrs = new Vector<LugerCompetitor>();
		cmptrs.add(l1);
		cmptrs.add(l2);
		cmptrs.add(l3);
		cmptrs.add(l4);
		cmptrs.add(l5);
		cmptrs.add(l6);
		cmptrs.add(l7);

		
		Competition competition = new Competition();
		competition.runsTimes = new Vector<Run>();
		
		Run run1 = new Run(cmptrs, (byte)1);
		Run run2 = new Run(cmptrs, (byte)1);
		
		competition.runsTimes.add(run1);
		competition.runsTimes.add(run2);
		
		t1 = ConvertMicrotime.toLocalTime(1230);	// 123 milisekund
		t2 = ConvertMicrotime.toLocalTime(12340);	// 1234 milisekundy -> 1 sekunda 234 milisekund
		t3 = ConvertMicrotime.toLocalTime(10);	// 1 milisekunda
		t4 = ConvertMicrotime.toLocalTime(2340);	// 234 milisekundy
		t5 = ConvertMicrotime.toLocalTime(50000);	// 5k milisekund -> 5 sekund
		t6 = ConvertMicrotime.toLocalTime(45330);   // 4533 milisekund -> 4 sekund 533 milisekund
		t7 = ConvertMicrotime.toLocalTime(120000);	// 12 sekund
		
		run1.run.put(l1, t1);
		run1.run.put(l2, t3);
		run1.run.put(l3, t4);
		run1.run.put(l4, t5);
		run1.run.put(l5, t6);
		run1.run.put(l6, t3);
		run1.run.put(l7, t2);

		run2.run.put(l1, t7);	// t1 + t7
		run2.run.put(l2, t6);	// t3 + t6
		run2.run.put(l3, t5);	// t4 + t5
		run2.run.put(l4, t4);	// t5 + t4
		run2.run.put(l5, t3);	// t6 + t3
		run2.run.put(l6, t2);	// t2 + t3
		run2.run.put(l7, t1);	// t1 + t2
		
		Map <LugerCompetitor, LocalTime> times = calc.calculateTotalRuntime(competition);
		
		LocalTime sl1 = times.get(l1);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(1230 + 120000), sl1);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(10 + 45330), times.get(l2));
		Assert.assertEquals(ConvertMicrotime.toLocalTime(2340 + 50000), times.get(l3));
		Assert.assertEquals(ConvertMicrotime.toLocalTime(2340 + 50000), times.get(l4));
		Assert.assertEquals(ConvertMicrotime.toLocalTime(45330 + 10), times.get(l5));
		Assert.assertEquals(ConvertMicrotime.toLocalTime(12340 + 10), times.get(l6));
		Assert.assertEquals(ConvertMicrotime.toLocalTime(1230 + 12340), times.get(l7));


		
	}

	@Test
	void testCalculateRanksFromTotalRuntimes() {		
		
		t1 = LocalTime.of(0, 0, 35);	// 6
		t2 = LocalTime.of(0, 0, 33);	// 4
		t3 = LocalTime.of(0, 0, 9);		// 1
		t4 = LocalTime.of(0, 0, 11);	// 2
		t5 = LocalTime.of(0, 0, 40);	// 7
		t6 = LocalTime.of(0, 0, 19);	// 3
		t7 = LocalTime.of(0, 0, 34);	// 5
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(out.get(l3), new Short((short) 1));
		Assert.assertEquals(out.get(l4), new Short((short) 2));
		Assert.assertEquals(out.get(l6), new Short((short) 3));
		Assert.assertEquals(out.get(l2), new Short((short) 4));
		Assert.assertEquals(out.get(l7), new Short((short) 5));
		Assert.assertEquals(out.get(l1), new Short((short) 6));

		}
	
	@Test
	void testCalculateRanksFromTotalRuntimes_eq() {
		t1 = LocalTime.of(0, 0, 35);	// 6
		t2 = LocalTime.of(0, 0, 33);	// 4
		t3 = LocalTime.of(0, 0, 9);		// 1
		t4 = LocalTime.of(0, 0, 11);	// 2
		t5 = LocalTime.of(0, 0, 40);	// 7
		t6 = LocalTime.of(0, 0, 11);	// 2
		t7 = LocalTime.of(0, 0, 34);	// 5
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(out.get(l3), new Short((short) 1));
		Assert.assertEquals(out.get(l4), new Short((short) 2));
		Assert.assertEquals(out.get(l6), new Short((short) 2));
		Assert.assertEquals(out.get(l2), new Short((short) 4));
		Assert.assertEquals(out.get(l7), new Short((short) 5));
		Assert.assertEquals(out.get(l1), new Short((short) 6));
		Assert.assertEquals(out.get(l5), new Short((short) 7));

		
	}

	@Test
	void testCalculateRanksFromTotalRuntimes_eq_double() {
		t1 = LocalTime.of(0, 0, 35);	// 5
		t2 = LocalTime.of(0, 0, 33);	// 4
		t3 = LocalTime.of(0, 0, 9);		// 1
		t4 = LocalTime.of(0, 0, 11);	// 2
		t5 = LocalTime.of(0, 0, 40);	// 7
		t6 = LocalTime.of(0, 0, 11);	// 2
		t7 = LocalTime.of(0, 0, 35);	// 5
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(out.get(l3), new Short((short) 1));
		Assert.assertEquals(out.get(l4), new Short((short) 2));
		Assert.assertEquals(out.get(l6), new Short((short) 2));
		Assert.assertEquals(out.get(l2), new Short((short) 4));
		Assert.assertEquals(out.get(l7), new Short((short) 5));
		Assert.assertEquals(out.get(l1), new Short((short) 5)); //4
		Assert.assertEquals(out.get(l5), new Short((short) 7));
		
	}
	
	@Test
	void testCalculateRanksFromTotalRuntimes_triple_eq() {
		t1 = LocalTime.of(0, 0, 35);	// 5
		t2 = LocalTime.of(0, 0, 11);	// 2
		t3 = LocalTime.of(0, 0, 9);		// 1
		t4 = LocalTime.of(0, 0, 11);	// 2
		t5 = LocalTime.of(0, 0, 40);	// 7
		t6 = LocalTime.of(0, 0, 11);	// 2
		t7 = LocalTime.of(0, 0, 35);	// 5
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(out.get(l3), new Short((short) 1));
		Assert.assertEquals(out.get(l4), new Short((short) 2));
		Assert.assertEquals(out.get(l6), new Short((short) 2));
		Assert.assertEquals(out.get(l2), new Short((short) 2));
		Assert.assertEquals(out.get(l7), new Short((short) 5));
		Assert.assertEquals(out.get(l1), new Short((short) 5));
		Assert.assertEquals(out.get(l5), new Short((short) 7));
		
	}
	
	@Test
	void testCalculateRanksFromTotalRuntimes_quad_eq() {
		t1 = LocalTime.of(0, 0, 11);	// 2
		t2 = LocalTime.of(0, 0, 11);	// 2
		t3 = LocalTime.of(0, 0, 9);		// 1
		t4 = LocalTime.of(0, 0, 11);	// 2
		t5 = LocalTime.of(0, 0, 40);	// 7
		t6 = LocalTime.of(0, 0, 11);	// 2
		t7 = LocalTime.of(0, 0, 35);	// 6
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(out.get(l3), new Short((short) 1));
		Assert.assertEquals(out.get(l4), new Short((short) 2));
		Assert.assertEquals(out.get(l6), new Short((short) 2));
		Assert.assertEquals(out.get(l2), new Short((short) 2));
		Assert.assertEquals(out.get(l7), new Short((short) 6));
		Assert.assertEquals(out.get(l1), new Short((short) 2));
		Assert.assertEquals(out.get(l5), new Short((short) 7));
		
	}
	
	@Test
	void testCalculateRanksFromTotalRuntimes_militimes() {
		t1 = ConvertMicrotime.toLocalTime(20000);	// 2 sekundy		-> 1
		t2 = ConvertMicrotime.toLocalTime(22000);	// 2 sekundy 200 milisekund	-> 3
		t3 = ConvertMicrotime.toLocalTime(22010);	// 2 sekundy 201 milisekund	-> 4
		t4 = ConvertMicrotime.toLocalTime(22010);	// 2 sekundy 201 milisekund	-> 4
		t5 = ConvertMicrotime.toLocalTime(20000);	// 2 sekundy		-> 1
		t6 = ConvertMicrotime.toLocalTime(120000);   // 4533 milisekund -> 4 sekund 533 milisekund	-> 6
		t7 = ConvertMicrotime.toLocalTime(120000);	// 12 sekund	-> 6
		
		test.put(l7, t7);
		test.put(l6, t6);
		test.put(l5, t5);
		test.put(l4, t4);
		test.put(l3, t3);
		test.put(l2, t2);
		test.put(l1, t1);
		
		CalculateRanksAfterRun calc = new CalculateRanksAfterRun();
		Map<LugerCompetitor, Short> out = calc.calculateRanksFromTotalRuntimes(test);
		
		Assert.assertEquals(new Short((short)1), out.get(l1));
		Assert.assertEquals(new Short((short)1), out.get(l5));
		Assert.assertEquals(new Short((short)3), out.get(l2));
		Assert.assertEquals(new Short((short)4), out.get(l3));
		Assert.assertEquals(new Short((short)4), out.get(l4));
		Assert.assertEquals(new Short((short)6), out.get(l6));
		Assert.assertEquals(new Short((short)6), out.get(l7));



	}
	
}
