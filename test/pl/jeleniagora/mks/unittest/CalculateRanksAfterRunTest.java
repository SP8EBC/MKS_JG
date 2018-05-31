package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.Assert;
import pl.jeleniagora.mks.factories.ClubsFactory;
import pl.jeleniagora.mks.factories.LugersFactory;
import pl.jeleniagora.mks.scoring.CalculateRanksAfterRun;
import pl.jeleniagora.mks.types.LugerCompetitor;

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
	
}
