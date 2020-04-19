package ttl.larku.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//TODO - Add an annotation to have the test run by SpringRunner

//This annotation is to make Spring create a WebApplicationContext
//rather than just an ApplicationContext.  You can then inject
//it into your test
@WebAppConfiguration

//TODO - Add an annotation to trigger scanning of your Configuration classes
//TODO - Add an annotation to set the ActiveProfile
public class TrackControllerTest {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@Test
	public void testGetAll() throws Exception {
		//TODO - Initialize ResultActions with the result of a mockMvc call to get one track 
		//     - Look at the main line projects for examples
		ResultActions actions = null; 

		//TODO - Add an expectation to check that the status is Ok

		//TODO - Add an expectation to check the size of the returned collection

		MvcResult result = actions.andReturn();
		System.out.println("result is " + result);
	}

	//Etc. Etc.
}
