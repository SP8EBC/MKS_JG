package pl.jeleniagora.mks.integrationtest;

/**
 * Test który ma zasymulować komunikację z chronometrem i sprawdzić czy pooprawnie działa zapisywanie czasów
 * ślizgów, i przechodzenie na następny ślizg w trybie automatycznym
 */
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Vector;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.chrono.ChronoStateMachine;
import pl.jeleniagora.mks.chrono.ConvertMicrotime;
import pl.jeleniagora.mks.events.AfterStartListGeneration;
import pl.jeleniagora.mks.events.ChangeCompetition;
import pl.jeleniagora.mks.events.EndOfRun;
import pl.jeleniagora.mks.events.LandedStateReached;
import pl.jeleniagora.mks.events.UpdateCurrentAndNextLuger;
import pl.jeleniagora.mks.exceptions.AppContextUninitializedEx;
import pl.jeleniagora.mks.exceptions.MissingCompetitionEx;
import pl.jeleniagora.mks.exceptions.StartOrderNotChoosenEx;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.gui.CompManager;
import pl.jeleniagora.mks.gui.CompManagerScoreTableModel;
import pl.jeleniagora.mks.rte.RTE_CHRONO;
import pl.jeleniagora.mks.rte.RTE_COM;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.serial.TypesConverters;
import pl.jeleniagora.mks.settings.ChronometerS;
import pl.jeleniagora.mks.settings.DisplayS;
import pl.jeleniagora.mks.settings.SpringS;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Reserve;
import pl.jeleniagora.mks.types.Run;

class AsavingTest {

	AnnotationConfigApplicationContext ctx;
	CompManager frame;
	
	Competitions competitions;
	
	Vector<Competition> cmps;
	
	@BeforeEach
	void setUp() throws Exception {
		
		ctx = new AnnotationConfigApplicationContext(SpringS.class);
		
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		
		ChronometerS.gateCount = 2;
		ChronometerS.autosave = true;
		DisplayS.setInhibitMessageAtEndOfRun(true);
		
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
		cmps = mdl.fillWithTestData(competitions, false);
		AfterStartListGeneration.process(competitions);
		
		mdl.fireTableDataChanged();
		
		rte_st.competitions.competitions = cmps;
	
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSimpleStartOrder() throws AppContextUninitializedEx, MissingCompetitionEx, InterruptedException, StartOrderNotChoosenEx, UninitializedCompEx, Reserve {
		RTE_GUI rte_gui = (RTE_GUI)ctx.getBean("RTE_GUI");
		RTE_ST rte_st = ctx.getBean(RTE_ST.class);
		RTE_COM rte_com = ctx.getBean(RTE_COM.class);
		RTE_CHRONO rte_chrono = ctx.getBean(RTE_CHRONO.class);
		
		LugerCompetitor expectedActual, extectedNext;
		LugerCompetitor currentActual, currentNext;
		
		Vector<Character> two_sec_1 = TypesConverters.convertStringToCharacterVector("CZI10908045a1");
		Vector<Character> two_sec_2 = TypesConverters.convertStringToCharacterVector("CZI30908065a1");
		
		Vector<Character> two_sec_200msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
		Vector<Character> two_sec_200msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a1");

		Vector<Character> two_sec_201msec_1 = TypesConverters.convertStringToCharacterVector("CZI1090904461");
		Vector<Character> two_sec_201msec_2 = TypesConverters.convertStringToCharacterVector("CZI30909065a2");
		
		LocalTime expected2sec = ConvertMicrotime.toLocalTime(20000);
		LocalTime expected2sec200msec = ConvertMicrotime.toLocalTime(22000);
		LocalTime expected2sec201msec = ConvertMicrotime.toLocalTime(22010);
		
		ChronoStateMachine act;
		LocalTime tim;

		// zmiana konkurencji
		LugerCompetitor cmpr = null;

		ChangeCompetition.changeActualCompetition(cmps.get(1), true);
		rte_gui.currentCompetition.setText(rte_st.currentCompetition.toString());
		
		// metoda changeActualCompetition zmienia tylko aktualną konkurencję w logice aplikacji (RTE_ST) ale nie aktualizuje
		// tabeli źródłowej dla JTable z wynikami ani nie przestawia competitionBeingShown. M.in. te dwie rzeczy są przestawiane
		// podczas używania pola rozwijanego "konkurencje" i założenie jest takie, że użytkownik najpierw wybierze a potem kliknie "Zmień konkurencje"
		rte_gui.competitionBeingShown = cmps.get(1);
		rte_gui.model.updateTableData(cmps.get(1), false);
		rte_gui.model.updateTableHeading(cmps.get(1), false);
		rte_gui.model.fireTableStructureChanged();
		rte_gui.model.fireTableDataChanged();
		
		cmpr = UpdateCurrentAndNextLuger.findFirstWithoutTime();
		UpdateCurrentAndNextLuger.setActualFromStartNumberAndNext(cmpr.getStartNumber());
		
		/*
		 * Podświetlanie aktualnie na torze
		 */
		Short actualRun = rte_st.currentRunCnt;
		rte_gui.compManager.markConreteRun(rte_st.actuallyOnTrack.getStartNumber(), actualRun);
		rte_gui.competitorClickedInTable = rte_st.actuallyOnTrack;
		rte_gui.runClickedInTable = rte_st.currentRun;
		
		// koniec zmiany konkurencji
		
		///////// sprawdzanie czy aktualnie na torze i nastepnie są dobrze ustawieni
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)1);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)2);
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		/////////////////////////////////////
		///////////// PIERWSZY ślizg ////////
		/////////////////////////////////////
		rte_com.rxBuffer = two_sec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2), tim);
		
		///// Koniec pierwszego ślizgu - czekanie na autozapis
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		LocalTime runtimeFor1 = rte_st.currentRun.run.get(rte_st.currentCompetition.invertedStartList.get((short)1));
		
		Assert.assertEquals(expected2sec, runtimeFor1);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)2);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)3);
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		////////////////////////////////////
		///////// DRUGI ślizg		////////
		///////////////////////////////////
		rte_com.rxBuffer = two_sec_200msec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_200msec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(22000), tim);		// 2 sekundy 200 milisekund
		////// koniec drugiego ślizgu -- czekanie na autozapis///
		
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		//ODCZYTYWANIE CZASU ŚLIZGU DLA DRUGIEGO
		LocalTime runtimeFor2 = rte_st.currentRun.run.get(rte_st.currentCompetition.invertedStartList.get((short)2)); 
		
		Assert.assertEquals(expected2sec200msec, runtimeFor2);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)3);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)4);
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		////////////////////////////////////
		///////// TRZECI ślizg		////////
		///////////////////////////////////
		rte_com.rxBuffer = two_sec_201msec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_201msec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(22010), tim);
		////// koniec trzeciego ślizgu -- czekanie na autozapis///

		
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		//ODCZYTYWANIE CZASU ŚLIZGU DLA TRZECIEGO
		LocalTime runtimeFor3 = rte_st.currentRun.run.get(rte_st.currentCompetition.invertedStartList.get((short)3)); 
		
		Assert.assertEquals(expected2sec201msec, runtimeFor3);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)4);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)5);			// wszystkich jest 5
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		////////////////////////////////////
		///////// CZWARTY ślizg		////////
		///////////////////////////////////
		rte_com.rxBuffer = two_sec_201msec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_201msec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(22010), tim);
		
		
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		//ODCZYTYWANIE CZASU ŚLIZGU DLA CZWARTEGO
		LocalTime runtimeFor4 = rte_st.currentRun.run.get(rte_st.currentCompetition.invertedStartList.get((short)4)); 
		
		Assert.assertEquals(expected2sec201msec, runtimeFor3);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)5);
		extectedNext = null;			// wszystkich jest 5 dlatego teraz następny ma być już ustawiony jako null
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		
		////////////////////////////////////
		///////// PIĄTY ślizg	-- EndOfRun	////////
		///////////////////////////////////
		rte_com.rxBuffer = two_sec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(LocalTime.of(0, 0, 2), tim);
		
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		//ODCZYTYWANIE CZASU ŚLIZGU DLA PIATEGO
		Run first = rte_st.currentCompetition.runsTimes.get(0);
		Run second = rte_st.currentCompetition.runsTimes.get(1);
		LocalTime runtimeFor5 = first.run.get(rte_st.currentCompetition.invertedStartList.get((short)5)); 
		
		Assert.assertEquals(expected2sec201msec, runtimeFor3);
		Assert.assertEquals(second, rte_st.currentRun);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)1);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)2);			// wszystkich jest 5 dlatego teraz następny ma być już ustawiony jako null
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		
		////////////////////////////////////////////////////////////
		///////// SZÓSTY ŁĄCZNIE ślizg	-- PIERSZY W DRUGIM	////////
		////////////////////////////////////////////////////////////
		rte_com.rxBuffer = two_sec_201msec_1;
		rte_com.rxDataAvaliable = true;
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		Assert.assertEquals(ChronoStateMachine.V1_ROTATE, act);
		
		rte_com.rxBuffer = two_sec_201msec_2;
		rte_com.rxDataAvaliable = true;		
		
		synchronized (rte_chrono.syncState) {
			try {
				rte_chrono.syncState.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			act = rte_chrono.timeMeasState;
		}
		
		synchronized (rte_chrono.syncRuntime) {
			try {
				rte_chrono.syncRuntime.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tim = rte_chrono.runTime;
		}
		
		Assert.assertEquals(ChronoStateMachine.LANDED, act);
		Assert.assertEquals(ConvertMicrotime.toLocalTime(22010), tim);
		
		Thread.sleep(6000, 0);	// autozapis ma delay 5 sekund
		
		//ODCZYTYWANIE CZASU ŚLIZGU
		LocalTime runtimeFor1_2 = rte_st.currentRun.run.get(rte_st.currentCompetition.invertedStartList.get((short)1)); 
		
		Assert.assertEquals(expected2sec201msec, runtimeFor1_2);
		Assert.assertEquals(second, rte_st.currentRun);
		
		expectedActual = rte_st.currentCompetition.invertedStartList.get((short)2);
		extectedNext = rte_st.currentCompetition.invertedStartList.get((short)3);
		
		currentActual = rte_st.actuallyOnTrack;
		currentNext = rte_st.nextOnTrack;
		
		Assert.assertEquals(expectedActual, currentActual);
		Assert.assertEquals(extectedNext, currentNext);
		
		//fail("Not yet implemented");
	}

}
