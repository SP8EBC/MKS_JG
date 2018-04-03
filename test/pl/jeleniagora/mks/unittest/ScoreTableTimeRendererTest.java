package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.jeleniagora.mks.gui.CompManagerScoreTableTimeRenderer;

class ScoreTableTimeRendererTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testPrepareString() {
		Integer testInt1 = new Integer(123450); // 12 sekund 345 milisekund
		Integer testInt2 = new Integer(680250); // 1 minuta 8 sekund 25 milisekund
		Integer testInt3 = new Integer(9990);   // 999 milisekund
		Integer testInt4 = new Integer(230120); // 23 sekund 12 milisekund
		Integer testInt5 = new Integer(6013440); // 10 minut 1 sekunda 344 milisekund
		
		String testE1 = "12.345";
		String testE2 = "1:08.025";
		String testE3 = "00.999";
		String testE4 = "23.012";
		String testE5 = "10:01.344";

		String testE10 = "12.345";
		String testE20 = "1:08.25";
		String testE30 = "00.999";
		String testE40 = "23.12";
		String testE50 = "10:01.344";
		
		String testS1, testS2, testS3, testS4, testS5;
		
		testS1 = CompManagerScoreTableTimeRenderer.prepareString(testInt1, true);
		testS2 = CompManagerScoreTableTimeRenderer.prepareString(testInt2, true);
		testS3 = CompManagerScoreTableTimeRenderer.prepareString(testInt3, true);
		testS4 = CompManagerScoreTableTimeRenderer.prepareString(testInt4, true);
		testS5 = CompManagerScoreTableTimeRenderer.prepareString(testInt5, true);

		Assert.assertEquals(testE1, testS1);
		Assert.assertEquals(testE2, testS2);
		Assert.assertEquals(testE3, testS3);
		Assert.assertEquals(testE4, testS4);
		Assert.assertEquals(testE5, testS5);
		
		testS1 = CompManagerScoreTableTimeRenderer.prepareString(testInt1, false);
		testS2 = CompManagerScoreTableTimeRenderer.prepareString(testInt2, false);
		testS3 = CompManagerScoreTableTimeRenderer.prepareString(testInt3, false);
		testS4 = CompManagerScoreTableTimeRenderer.prepareString(testInt4, false);
		testS5 = CompManagerScoreTableTimeRenderer.prepareString(testInt5, false);

		Assert.assertEquals(testE10, testS1);
		Assert.assertEquals(testE20, testS2);
		Assert.assertEquals(testE30, testS3);
		Assert.assertEquals(testE40, testS4);
		Assert.assertEquals(testE50, testS5);
		
		
//		fail("Not yet implemented");
	}

}
