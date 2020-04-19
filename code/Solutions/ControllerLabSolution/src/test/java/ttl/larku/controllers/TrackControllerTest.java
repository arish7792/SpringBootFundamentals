package ttl.larku.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ttl.larku.domain.Track;
import ttl.larku.jconfig.LarkUConfig;
import ttl.larku.jconfig.LarkUServletConfig;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {LarkUConfig.class, LarkUServletConfig.class})
@ActiveProfiles("development")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

        track.setTitle("Only Lightness");

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
        actions = actions.andExpect(jsonPath("$.title", Matchers.containsString("Only Lightness")));
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
