package ttl.larku.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.LarkUTestDataConfig;
import ttl.larku.controllers.rest.StudentRestController;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;
import ttl.larku.service.StudentService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class StudentRestControllerMvcTest {

	// @Resource
	@Autowired
	private StudentRestController studentController;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MockMvc mockMvc;

	private final int goodStudentId = 1;
	private final int badStudentId = 10000;



	@Before
	public void init() {
	}

	@Test
	public void testGetOneStudentGoodJson() throws Exception {
		MediaType accept = MediaType.APPLICATION_JSON_UTF8;

		MockHttpServletRequestBuilder request = get("/adminrest/student/{id}", goodStudentId);

		List<ResultMatcher> matchers = Arrays
				.asList(status().isOk(), 
						content().contentType(accept),
				jsonPath("$.entity.name").value(containsString("Manoj")));

		String jsonString = makeCall(request, accept, accept, matchers, null);
		System.out.println("One student good resp = " + jsonString);
	}

	@Test
	public void testGetOneStudentBadId() throws Exception {

		ResultActions actions = mockMvc
				.perform(get("/adminrest/student/{id}", badStudentId)
						.accept(MediaType.APPLICATION_JSON));

		MvcResult mvcr = actions
				.andExpect(status().is4xxClientError())
				.andReturn();
	}

	@Test
	public void testAddStudentGood() throws Exception {

		Student student = new Student("Yogita");
		student.setPhoneNumber("202 383-9393");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(student);

		ResultActions actions = mockMvc.perform(post("/adminrest/student/").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(jsonString));

		actions = actions.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

		actions = actions.andExpect(status().isCreated());

		actions = actions.andExpect(jsonPath("$.entity.name").value(Matchers.containsString("Yogita")));
		

		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();

		jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
		
	}

	@Test
	public void testGetAllStudentsGood() throws Exception {

		ResultActions actions = mockMvc.perform(get("/adminrest/student/").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$.entity", hasSize(4)));
		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();

		String jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
	}
	


	private String makeCall(MockHttpServletRequestBuilder builder, MediaType accept, MediaType contentType,
			List<ResultMatcher> matchers, String content) throws Exception {
		if (accept != null) {
			builder = builder.accept(accept);
		}
		if (contentType != null) {
			builder = builder.contentType(contentType);
		}
		if (content != null) {
			builder = builder.content(content);
		}

		ResultActions actions = mockMvc.perform(builder);

		for (ResultMatcher m : matchers) {
			actions = actions.andExpect(m);
		}

		// Get the result and return it
		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();
		String jsonString = response.getContentAsString();

		return jsonString;
	}

}
