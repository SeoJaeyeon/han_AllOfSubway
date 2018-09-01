package kr.ac.han;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
@WebAppConfiguration
public class WatsonTest {
Logger logger=LoggerFactory.getLogger(SubwayTest.class);
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mock;
	
	@Before
	public void beforeTest(){
		logger.info("/watson before");
		mock=MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testSample() throws Exception{
		logger.info("/watson 호출");
        this.mock.perform(
                get("/watson")
                .param("query", "홍대입구역 언제와?"))
                .andDo(print())
                .andExpect(status().isOk());
    
	}

	@After
	public void afterTest(){
		logger.info("/watson after");
	}
}
