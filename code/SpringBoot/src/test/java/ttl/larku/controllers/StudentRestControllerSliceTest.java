package ttl.larku.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ttl.larku.LarkUTestDataConfig;
import ttl.larku.controllers.rest.StudentRestController;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;
import ttl.larku.service.StudentService;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = StudentRestController.class)
public class StudentRestControllerSliceTest {

	@MockBean
	private StudentService studentService;

//	@InjectMocks
//	private StudentRestController studentController;

    @Autowired
	private MockMvc mockMvc;

	private final int goodStudentId = 1;
	private final int badStudentId = 10000;



	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		List<Student> students = Arrays.asList(new Student("Manoj", "282 929 9292", Student.Status.FULL_TIME),
				new Student("Alice", "393 9393 030", Student.Status.HIBERNATING));
		//Create a mock for the one controller we want to test.

		Mockito.when(studentService.getAllStudents()).thenReturn(students);
		Mockito.when(studentService.getStudent(goodStudentId)).thenReturn(students.get(0));
		Mockito.when(studentService.getStudent(badStudentId)).thenReturn(null);
		Student student = new Student("Yogita");
		Mockito.when(studentService.createStudent(any(Student.class))).thenReturn(student);
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

	public void testGetOneStudentHeader() throws Exception {
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

		MvcResult result = actions
				.andExpect(status().is4xxClientError())
				.andReturn();

		MockHttpServletResponse response = result.getResponse();

		String jsonString = response.getContentAsString();
		System.out.println("resp = " + jsonString);
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
	public void testAddStudentWithNoContentType() throws Exception {

		Student student = new Student("Yogita");
		student.setPhoneNumber("202 383-9393");
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(student);

		//ResultActions actions = mockMvc.perform(post("/adminrest/student/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(jsonString));
		ResultActions actions = mockMvc.perform(post("/adminrest/student/")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonString));

		actions = actions.andExpect(status().isUnsupportedMediaType());

	}

	@Test
	public void testGetAllStudentsGood() throws Exception {

		ResultActions actions = mockMvc.perform(get("/adminrest/student/").accept(MediaType.APPLICATION_JSON));

		actions = actions.andExpect(status().isOk());

		actions = actions.andExpect(jsonPath("$.entity", hasSize(2)));
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
