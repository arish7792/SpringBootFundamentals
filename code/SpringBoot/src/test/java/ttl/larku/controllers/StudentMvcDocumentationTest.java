package ttl.larku.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.controllers.rest.StudentRestController;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;
import ttl.larku.service.StudentService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class StudentMvcDocumentationTest {

	// @Resource
	@Autowired
	private StudentRestController studentController;
	
	@Autowired 
	private StudentService studentService;


	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	private final int goodStudentId = 1;
	private final int badStudentId = 10000;
	private String name1 = "Manoj";
	private String name2 = "Ana";
	private String newName = "Karl Jung";
	private String phoneNumber1 = "290 298 4790";
	private String phoneNumber2 = "3838 939 93939";
	private Student student1;
	private Student student2;


	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.apply(documentationConfiguration(this.restDocumentation)) 
				.build();

		studentService.clear();

		student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2 = new Student(name2, phoneNumber2, Status.HIBERNATING);
		
		studentService.createStudent(student1);
		studentService.createStudent(student2);
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

	/*-
	@Test
	public void testGetOneStudentGoodXml() throws Exception {
		MediaType accept = MediaType.APPLICATION_XML;

		MockHttpServletRequestBuilder request = get("/adminrest/student/{id}", goodStudentId);

		List<ResultMatcher> matchers = Arrays.asList(status().isOk(), 
				content().contentType(accept),
				xpath("/student/name").string("Manoj"));

		String jsonString = makeCall(request, accept, accept, matchers, null);
		System.out.println("resp = " + jsonString);

	}
	*/

	@Test
	public void testGetOneStudentBadId() throws Exception {

		ResultActions actions = mockMvc
				.perform(get("/adminrest/student/{id}", badStudentId)
						.accept(MediaType.APPLICATION_JSON));

		MvcResult mvcr = actions
				.andExpect(status().is4xxClientError())
				.andDo(document("oneStudentBadId"))
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
		
		actions.andDo(document("addStudent"));

		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();

		jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
		
	}

	@Test
	public void testGetAllStudentsGood() throws Exception {

		ResultActions actions = mockMvc.perform(get("/adminrest/student/").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$.entity", hasSize(2)));
		actions.andDo(document("students"));
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

		actions.andDo(document("student"));
		// Get the result and return it
		MvcResult result = actions.andReturn();

		MockHttpServletResponse response = result.getResponse();
		String jsonString = response.getContentAsString();

		return jsonString;
	}

}
