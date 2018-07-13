package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.Assert;
import pl.jeleniagora.mks.gui.CompManagerScoreTableModel;
import pl.jeleniagora.mks.start.order.FilOrder;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;

class FilStartOrderTest {

	Competition competition = null;
	
	@BeforeEach
	void setUp() throws Exception {
		CompManagerScoreTableModel mdl = new CompManagerScoreTableModel();	// tu jest metoda generująca losowe dane
		Competitions cmps = new Competitions();
		
		Vector<Competition> vc = mdl.fillWithTestData(cmps, true);		// generowanie losowych danych
		
		competition = vc.get(1);
		
		competition.ranks = new HashMap<LugerCompetitor, Short>();	// tworzenie nowej mapy z wynikami
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	// test bez lokat ex-aequo
	@Test
	void testNextStartNumberShortCompetition_nexa() {
		
		Short actual;
		
		LugerCompetitor l1 = competition.invertedStartList.get((short)1);
		LugerCompetitor l2 = competition.invertedStartList.get((short)2);
		LugerCompetitor l3 = competition.invertedStartList.get((short)3);
		LugerCompetitor l4 = competition.invertedStartList.get((short)4);
		LugerCompetitor l5 = competition.invertedStartList.get((short)5);
		
		// sprawdzanie czy numery startowe się zgadzają
		Assert.assertEquals(1, l1.getStartNumber());
		Assert.assertEquals(2, l2.getStartNumber());
		Assert.assertEquals(3, l3.getStartNumber());
		Assert.assertEquals(4, l4.getStartNumber());
		Assert.assertEquals(5, l5.getStartNumber());

		
		competition.ranks.put(l5, (short) 5);
		competition.ranks.put(l4, (short) 4);
		competition.ranks.put(l3, (short) 3);
		competition.ranks.put(l2, (short) 2);
		competition.ranks.put(l1, (short) 1);
		
		FilOrder order = new FilOrder();
		
		actual = order.nextStartNumber((short) 5, competition);
		Assert.assertEquals(new Short((short) 4), actual);
		
		actual = order.nextStartNumber((short) 4, competition);
		Assert.assertEquals(new Short((short) 3), actual);

		actual = order.nextStartNumber((short) 3, competition);
		Assert.assertEquals(new Short((short) 2), actual);
		
		actual = order.nextStartNumber((short) 2, competition);
		Assert.assertEquals(new Short((short) 1), actual);
		
		actual = order.nextStartNumber((short) 1, competition);
		Assert.assertEquals(null, actual);
		//fail("Not yet implemented");
	}
	
	// pierwszy test ex-aequo
	@Test
	void testNextStartNumberShortCompetition_exa1() {
		
		Short actual;
		
		LugerCompetitor l1 = competition.invertedStartList.get((short)1);
		LugerCompetitor l2 = competition.invertedStartList.get((short)2);
		LugerCompetitor l3 = competition.invertedStartList.get((short)3);
		LugerCompetitor l4 = competition.invertedStartList.get((short)4);
		LugerCompetitor l5 = competition.invertedStartList.get((short)5);
		
		// sprawdzanie czy numery startowe się zgadzają
		Assert.assertEquals(1, l1.getStartNumber());
		Assert.assertEquals(2, l2.getStartNumber());
		Assert.assertEquals(3, l3.getStartNumber());
		Assert.assertEquals(4, l4.getStartNumber());
		Assert.assertEquals(5, l5.getStartNumber());
		
		competition.ranks.put(l5, (short) 5);
		competition.ranks.put(l4, (short) 4);
		competition.ranks.put(l3, (short) 2);
		competition.ranks.put(l2, (short) 4);
		competition.ranks.put(l1, (short) 1);
		
		FilOrder order = new FilOrder();
		
		actual = order.nextStartNumber((short) 5, competition);
		Assert.assertEquals(new Short((short) 4), actual);
		
		actual = order.nextStartNumber((short) 4, competition);
		Assert.assertEquals(new Short((short) 2), actual);		

		actual = order.nextStartNumber((short) 3, competition);
		Assert.assertEquals(new Short((short) 1), actual);
		
		actual = order.nextStartNumber((short) 2, competition);
		Assert.assertEquals(new Short((short) 3), actual);		// 1?
		
		actual = order.nextStartNumber((short) 1, competition);
		Assert.assertEquals(null, actual);
		//fail("Not yet implemented");
	}
	
	// drugi test ex-aequo
	@Test
	void testNextStartNumberShortCompetition_exa2() {
		
		Short actual;
		
		LugerCompetitor l1 = competition.invertedStartList.get((short)1);
		LugerCompetitor l2 = competition.invertedStartList.get((short)2);
		LugerCompetitor l3 = competition.invertedStartList.get((short)3);
		LugerCompetitor l4 = competition.invertedStartList.get((short)4);
		LugerCompetitor l5 = competition.invertedStartList.get((short)5);
		
		// sprawdzanie czy numery startowe się zgadzają
		Assert.assertEquals(1, l1.getStartNumber());
		Assert.assertEquals(2, l2.getStartNumber());
		Assert.assertEquals(3, l3.getStartNumber());
		Assert.assertEquals(4, l4.getStartNumber());
		Assert.assertEquals(5, l5.getStartNumber());
		
		competition.ranks.put(l5, (short) 5);
		competition.ranks.put(l4, (short) 4);
		competition.ranks.put(l3, (short) 4);
		competition.ranks.put(l2, (short) 4);
		competition.ranks.put(l1, (short) 1);
		
		FilOrder order = new FilOrder();
		
		actual = order.nextStartNumber((short) 5, competition);
		Assert.assertEquals(new Short((short) 4), actual);
		
		actual = order.nextStartNumber((short) 4, competition);
		Assert.assertEquals(new Short((short) 3), actual);		// 2?

		actual = order.nextStartNumber((short) 3, competition);
		Assert.assertEquals(new Short((short) 2), actual);
		
		actual = order.nextStartNumber((short) 2, competition);
		Assert.assertEquals(new Short((short) 1), actual);		
		
		actual = order.nextStartNumber((short) 1, competition);
		Assert.assertEquals(null, actual);
		//fail("Not yet implemented");
	}
	
	// trzeci test ex-aequo
	@Test
	void testNextStartNumberShortCompetition_exa3() {
		
		Short actual;
		
		LugerCompetitor l1 = competition.invertedStartList.get((short)1);
		LugerCompetitor l2 = competition.invertedStartList.get((short)2);
		LugerCompetitor l3 = competition.invertedStartList.get((short)3);
		LugerCompetitor l4 = competition.invertedStartList.get((short)4);
		LugerCompetitor l5 = competition.invertedStartList.get((short)5);
		
		// sprawdzanie czy numery startowe się zgadzają
		Assert.assertEquals(1, l1.getStartNumber());
		Assert.assertEquals(2, l2.getStartNumber());
		Assert.assertEquals(3, l3.getStartNumber());
		Assert.assertEquals(4, l4.getStartNumber());
		Assert.assertEquals(5, l5.getStartNumber());
		
		competition.ranks.put(l5, (short) 5);
		competition.ranks.put(l4, (short) 4);
		competition.ranks.put(l3, (short) 2);
		competition.ranks.put(l2, (short) 3);
		competition.ranks.put(l1, (short) 2);
		
		FilOrder order = new FilOrder();
		
		actual = order.nextStartNumber((short) 5, competition);
		Assert.assertEquals(new Short((short) 4), actual);
		
		actual = order.nextStartNumber((short) 4, competition);
		Assert.assertEquals(new Short((short) 2), actual);	

		actual = order.nextStartNumber((short) 3, competition);
		Assert.assertEquals(new Short((short) 1), actual);
		
		actual = order.nextStartNumber((short) 2, competition);
		Assert.assertEquals(new Short((short) 3), actual);		
		
		actual = order.nextStartNumber((short) 1, competition);
		Assert.assertEquals(null, actual);
		//fail("Not yet implemented");
	}
/*
	@Test
	void testNextStartNumberLugerCompetitorCompetition() {
		fail("Not yet implemented");
	}

	@Test
	void testNextStartLuger() {
		fail("Not yet implemented");
	}

	@Test
	void testGetFirst() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSecond() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckIfLastInRunLugerCompetitorCompetitionRun() {
		fail("Not yet implemented");
	}

	@Test
	void testCheckIfLastInRunShortCompetition() {
		fail("Not yet implemented");
	}
*/
}
