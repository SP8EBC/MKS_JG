package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.junit.Assert;

import pl.jeleniagora.mks.events.AfterStartListGeneration;
import pl.jeleniagora.mks.events.EndOfRun;
import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.EndOfCompEx;
import pl.jeleniagora.mks.exceptions.EndOfRunEx;
import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.gui.CompManagerScoreTableModel;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.SpringS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;

class EndOfRunTest {
	
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
			EndOfRun.setAppCtx(ctx);
			
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
			
			competitions = new Competitions("test", LocalDate.now());
			Vector<Competition> cmps = mdl.fillWithTestData(competitions, true);
			AfterStartListGeneration.process(competitions);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSwitchToNextRun() throws InterruptedException, EndOfCompEx {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
		
		boolean runEnd = false;
		
		Assert.assertEquals(rte_st.currentRunCnt, 0);
		Assert.assertEquals(rte_st.currentRun, rte_st.currentCompetition.runsTimes.get(0));

		UpdateCurrentAndNextLuger.rewindToBegin();
		
		try {
			UpdateCurrentAndNextLuger.moveForwardNormally();
			
		} catch (EndOfRunEx | AppContextUninitializedEx e) {
			e.printStackTrace();
		}
		
		try {
			UpdateCurrentAndNextLuger.moveForwardNormally();
		} catch (EndOfRunEx | AppContextUninitializedEx e) {
			runEnd = true;
		}
		
		if (runEnd) {
			EndOfRun.process();
		}
		else {
			fail("failed");
		}
		
		Assert.assertEquals(rte_st.currentRunCnt, 1);
		Assert.assertEquals(rte_st.currentRun, rte_st.currentCompetition.runsTimes.get(1));
		
		


	}

}
