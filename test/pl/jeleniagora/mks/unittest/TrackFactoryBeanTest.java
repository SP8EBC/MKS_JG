package pl.jeleniagora.mks.unittest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.Assert;
import pl.jeleniagora.mks.types.Track;

class TrackFactoryBeanTest {

    ApplicationContext context;

//    @Autowired
//    @Qualifier("karpacz")
//    Track track;
	
	@BeforeEach
	void setUp() throws Exception {
		context = new ClassPathXmlApplicationContext("luge-tracks-spring-ctx.xml");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Track track;
		track = (Track)context.getBean("karpacz");
		
		Assert.assertEquals(new String("Karpacz"), track.name);
	}

}
