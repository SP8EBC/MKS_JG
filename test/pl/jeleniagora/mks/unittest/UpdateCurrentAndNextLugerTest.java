package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.junit.Assert;

import pl.jeleniagora.mks.events.AfterStartListGeneration;
import pl.jeleniagora.mks.events.ChangeCompetition;
import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.exceptions.MissingCompetitionEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.gui.CompManagerCSelectorUpdater;
import pl.jeleniagora.mks.gui.CompManagerScoreTableModel;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.SpringS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

class UpdateCurrentAndNextLugerTest {
	
	AnnotationConfigApplicationContext ctx;
	CompManager frame;
	
	Competitions competitions;

	@BeforeEach
	void setUp() throws Exception {
		ctx = new AnnotationConfigApplicationContext(SpringS.class);
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		
		ChronometerS.gateCount = 2;
		
		LandedStateReached.setAppCtx(ctx);
		UpdateCurrentAndNextLuger.setAppCtx(ctx);
		ChangeCompetition.setAppCtx(ctx);
		
		CompManager.utMain(ctx);
		
		System.out.println("Waiting for the CompManager to became fully operational.. ");
		
		synchronized(rte_gui.syncCompManagerRdy) {
			try {
				rte_gui.syncCompManagerRdy.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		frame = rte_gui.compManager;
		
		CompManagerScoreTableModel mdl = (CompManagerScoreTableModel)frame.getScoreTableModel();
		
//		competitions = new Competitions("test", LocalDate.now());
//		Vector<Competition> cmps = mdl.fillWithTestData(competitions, true);
//		AfterStartListGeneration.process(competitions);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testSetActualFromStartNumberAndNext() throws AppContextUninitializedEx, MissingCompetitionEx, StartOrderNotChoosenEx {
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		
		CompManagerScoreTableModel mdl = (CompManagerScoreTableModel)frame.getScoreTableModel();
		CompManagerCSelectorUpdater selectorUpdater = new CompManagerCSelectorUpdater(ctx);

		
		/* 
		 * W tym teście lista startowa musi być zainicjalizowana zerami za wyjątkiem kilku przypadków w których będzie
		 * jakiś czas
		 */
		Vector<Competition> cmps = mdl.fillWithTestData(rte_st.competitions, false);
		AfterStartListGeneration.process(rte_st.competitions);
		((CompManagerScoreTableModel)frame.getScoreTableModel()).fireTableDataChanged();
				
		rte_st.competitions.competitions = cmps;
		
		ChangeCompetition.changeActualCompetition(cmps.get(1), true);
		
		Vector<Run> runsFrom1stCmp = cmps.get(1).runsTimes;	// wszystkie ślizgi z drugiej konkurencji - "duża"
			
		Map<Short, LugerCompetitor> startList = cmps.get(1).invertedStartList;	// odwrócon lista startowa
		
		Run stTraining = runsFrom1stCmp.get(0);	// czasy pierwszego ślizgu w drugiej konkurencji
				
		for (short i = 0; i < stTraining.totalTimes.size(); i++) {
			if (i > 1 && i < 4) {
				
				LocalTime time = LocalTime.of(0, 0, 55);
				LugerCompetitor cmpr = startList.get(i);
				
				stTraining.totalTimes.put(cmpr, time);	// dociepywanie losowego czsu do niektórych zawodników
				
			}
		}
		selectorUpdater.updateSelectorContent(cmps);

		((CompManagerScoreTableModel)frame.getScoreTableModel()).fireTableDataChanged();
		
		/*
		 * Tutaj zaczyna się faktyczny test jednostkowy
		 */
		
		LugerCompetitor first = UpdateCurrentAndNextLuger.findFirstWithoutTime();
		UpdateCurrentAndNextLuger.setActualFromStartNumberAndNext(first.getStartNumber());
		
		Assert.assertEquals(rte_st.actuallyOnTrack.getStartNumber(), 1);
		Assert.assertEquals(rte_st.nextOnTrack.getStartNumber(), 4);

	}

	@Test
	void testFindFirstWithoutTime() throws AppContextUninitializedEx, MissingCompetitionEx, StartOrderNotChoosenEx {
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		
		LocalTime zero = LocalTime.of(0, 0, 0, 0);

		CompManagerScoreTableModel mdl = (CompManagerScoreTableModel)frame.getScoreTableModel();
		CompManagerCSelectorUpdater selectorUpdater = new CompManagerCSelectorUpdater(ctx);

		
		/* 
		 * W tym teście lista startowa musi być zainicjalizowana zerami za wyjątkiem kilku przypadków w których będzie
		 * jakiś czas
		 */
		Vector<Competition> cmps = mdl.fillWithTestData(rte_st.competitions, false);
		AfterStartListGeneration.process(rte_st.competitions);
		((CompManagerScoreTableModel)frame.getScoreTableModel()).fireTableDataChanged();
				
		rte_st.competitions.competitions = cmps;
		
		ChangeCompetition.changeActualCompetition(cmps.get(1), true);
		
		Vector<Run> runsFrom1stCmp = cmps.get(1).runsTimes;	// wszystkie ślizgi z drugiej konkurencji - "duża"
			
		Map<Short, LugerCompetitor> startList = cmps.get(1).invertedStartList;	// odwrócon lista startowa
		
		Run stTraining = runsFrom1stCmp.get(0);	// czasy pierwszego ślizgu w drugiej konkurencji
				
		for (short i = 0; i < stTraining.totalTimes.size(); i++) {
			if (i > 0 && i < 4) {
				
				LocalTime time = LocalTime.of(0, 0, 55);
				LugerCompetitor cmpr = startList.get(i);
				
				stTraining.totalTimes.put(cmpr, time);	// dociepywanie losowego czsu do niektórych zawodników
				
			}
		}
		selectorUpdater.updateSelectorContent(cmps);

		((CompManagerScoreTableModel)frame.getScoreTableModel()).fireTableDataChanged();
		
		
		/*
		 * Ustawianie drugiemu saneczkarzowi czasu jako zero
		 */
//		rte_st.currentRun.run.replaceAll(				
//				(LugerCompetitor k, LocalTime v) -> {
//					if (k.getStartNumber() == 2)
//						return zero;
//					else return v;
//				}
//		);
		
		LugerCompetitor r = UpdateCurrentAndNextLuger.findFirstWithoutTime();
		LugerCompetitor s = rte_st.currentCompetition.invertedStartList.get((Short)(short)4);
		
		Assert.assertEquals(r, s);
		
	}
	
	@Test
	void testRewindToBegin() {
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		boolean runEnd = false;
		
		/*
		 * Przewijanie do początku konkurencji
		 */
		UpdateCurrentAndNextLuger.rewindToBegin();
		
		Assert.assertEquals(rte_st.actuallyOnTrack, rte_st.currentCompetition.invertedStartList.get((short)1));
		Assert.assertEquals(rte_st.nextOnTrack, rte_st.currentCompetition.invertedStartList.get((short)2));
		

		
		
		
		
		
	}

	@Test
	void testMoveForwardNormally() {
		
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		boolean runEnd = false;
		
		CompManagerScoreTableModel mdl = (CompManagerScoreTableModel)frame.getScoreTableModel();
		
		Vector<Competition> cmps = mdl.fillWithTestData(rte_st.competitions, false);
		mdl.fireTableDataChanged();
		AfterStartListGeneration.process(rte_st.competitions);
		
		try {
			ChangeCompetition.changeActualCompetition(cmps.get(1), true);
		} catch (AppContextUninitializedEx | MissingCompetitionEx e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		/*
		 * Przewijanie do początku konkurencji
		 */
		UpdateCurrentAndNextLuger.rewindToBegin();
		
		try {
			UpdateCurrentAndNextLuger.moveForwardNormally();
		} catch (EndOfRunEx | AppContextUninitializedEx | StartOrderNotChoosenEx e) {
			fail("Exception has been thrown");
		}


		
		Assert.assertEquals(rte_st.actuallyOnTrack, rte_st.currentCompetition.invertedStartList.get((short)2));
		Assert.assertEquals(rte_st.nextOnTrack, rte_st.currentCompetition.invertedStartList.get((short)3));
		
		try {
			UpdateCurrentAndNextLuger.moveForwardNormally();
		} catch (EndOfRunEx | AppContextUninitializedEx | StartOrderNotChoosenEx e) {
			// tutaj wyjątek powinien zostać rzucony bo przeszlo do ostatniej konkurencji
			runEnd = true;

		}

		Assert.assertEquals(rte_st.actuallyOnTrack, rte_st.currentCompetition.invertedStartList.get((short)3));
		Assert.assertEquals(rte_st.nextOnTrack, rte_st.currentCompetition.invertedStartList.get((short)4));
		
//		Assert.assertEquals(true, runEnd);	
		
	}

}
