package ttl.larku.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import ttl.larku.domain.Track;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//This annotation makes the test run with the same Spring context
//As our main application 
@SpringBootTest
//To be able to inject MockMvc
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TrackControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void init() {
	}
	
	@Test
	public void testGetAll() throws Exception {
		
		ResultActions actions = mockMvc.perform(get("/track").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$", hasSize(6)));
		MvcResult result = actions.andReturn();
		System.out.println("result is " + result);
	}

	@Test
	public void testGetOneGood() throws Exception {
		
		ResultActions actions = mockMvc.perform(get("/track/{id}", 1).accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		//actions = actions.andExpect(jsonPath("$.title", hasSize(6)));
		actions = actions.andExpect(jsonPath("$.title", Matchers.containsString("Shadow")));
		MvcResult result = actions.andReturn();
		System.out.println("result is " + result);
	}

	@Test
	public void testGetOneBad() throws Exception {
		
		ResultActions actions = mockMvc.perform(get("/track/{id}", 10000000).accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isBadRequest());
		actions.andReturn();
	}

	@Test
	public void testUpdate() throws Exception {
		ResultActions actions = mockMvc.perform(get("/track/{id}", 1).accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		//actions = actions.andExpect(jsonPath("$.title", hasSize(6)));
		actions = actions.andExpect(jsonPath("$.title", Matchers.containsString("Shadow")));
		MvcResult mvcr = actions.andReturn();
		String origString = mvcr.getResponse().getContentAsString();
		System.out.println("orig String is " + origString);
		
		ObjectMapper mapper = new ObjectMapper();
		Track track = mapper.readValue(origString, Track.class);
		
		String changedTitle = "Only Lightness";
		
		track.setTitle(changedTitle);
		
		String newString = mapper.writeValueAsString(track);

		ResultActions putActions = mockMvc.perform(put("/track/")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(newString));
		putActions = putActions.andExpect(status().isNoContent());
		mvcr = putActions.andReturn();

		actions = mockMvc.perform(get("/track/{id}", 1).accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		//actions = actions.andExpect(jsonPath("$.title", hasSize(6)));
		actions = actions.andExpect(jsonPath("$.title", Matchers.containsString(changedTitle)));
		actions.andReturn();
	}

	@Test
	public void testDelete() throws Exception {
		ResultActions actions = mockMvc.perform(delete("/track/{id}", 1)
				.accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isNoContent());
		actions.andReturn();

		actions = mockMvc.perform(get("/track/{id}", 1)
				.accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isBadRequest());
		actions.andReturn();
	}

	@Test
	public void testPost() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Track track = Track.album("Silent Days").artist("Josh Streenberg").title("Silent Number One").build();
		ResultActions actions = mockMvc.perform(
				post("/track")
						.content(mapper.writeValueAsString(track))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		);


		actions = actions.andExpect(status().isCreated());

		//actions = actions.andExpect(jsonPath("$.title", hasSize(6)));
		actions = actions.andExpect(jsonPath("$.title",
				Matchers.containsString("Silent Number One")));

		MvcResult mvcr = actions.andReturn();
		String origString = mvcr.getResponse().getContentAsString();
		System.out.println("orig String is " + origString);

		Track newTrack = mapper.readValue(origString, Track.class);
		System.out.println("New Track: " + newTrack);

		actions = mockMvc.perform(get("/track").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$", hasSize(7)));
	}
	//Etc. Etc.
}
