package ttl.larku.controllers;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.controllers.rest.RestResultGeneric;
import ttl.larku.controllers.rest.RestResultGeneric.Status;
import ttl.larku.domain.RestResult;
import ttl.larku.domain.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)

//Populate your DB.  You can either do it this way or use the schema[-XXX].sql and data[-XXX].sql files
//and @DirtiesContext as we are doing below
//@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql", "/ttl/larku/db/populateDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//The DirtiesContext will recreate the context after every test.
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class RestClientSpringTest {

    @LocalServerPort
    private int port; 

    @Autowired
    private TestRestTemplate rt;
    @Autowired
	private ObjectMapper mapper; 

	// GET with url parameters
	private String rootUrl;
	private String oneStudentUrl;

	@Before
	public void setup() {
		rootUrl = "http://localhost:" + port + "/adminrest/student";
		oneStudentUrl = rootUrl + "/{id}";
	}

	@Test
	public void testGetOneStudentUsingAutoUnmarshalling() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<RestResult> response = rt.exchange(oneStudentUrl, 
        		HttpMethod.GET, entity, RestResult.class, 2);
		assertEquals(200, response.getStatusCodeValue());

		RestResult rr = response.getBody();
		RestResult.Status status = rr.getStatus();
		assertTrue(status == RestResult.Status.Ok);

		//Still need the mapper to convert the entity Object
		//which should be represented by a map of student properties
		Student s = mapper.convertValue(rr.getEntity(), Student.class);
		System.out.println("Student is " + s);
		assertTrue(s.getName().contains("Ana"));
	}

	@Test
	public void testGetOneStudentWithManualJson() throws IOException {
		ResponseEntity<String> response = rt.getForEntity(oneStudentUrl, String.class, 2);
		assertEquals(200, response.getStatusCodeValue());

		String raw = response.getBody();
		JsonNode root = mapper.readTree(raw);
		Status status = Status.valueOf(root.path("status").asText());
		assertTrue(status == Status.Ok);

		JsonNode entity = root.path("entity");
		Student s = mapper.treeToValue(entity, Student.class);
		System.out.println("Student is " + s);
		assertTrue(s.getName().contains("Ana"));
	}

	@Test
	public void testGetOneStudentBadId() throws IOException {
		ResponseEntity<String> response = rt.getForEntity(oneStudentUrl, String.class, 10000);
		assertEquals(400, response.getStatusCodeValue());

		String raw = response.getBody();
		JsonNode root = mapper.readTree(raw);
		Status status = Status.valueOf(root.path("status").asText());
		assertTrue(status == Status.Error);
		
		JsonNode errors = root.path("errors");
		assertTrue(errors != null);

		StringBuffer sb = new StringBuffer(100);
		errors.forEach(node -> {
			sb.append(node.asText());
		});
		String reo = sb.toString();
		System.out.println("Error is " + reo);
		assertTrue(reo.contains("not found"));
	}

	@Test
	public void testGetAllUsingAutoUnmarshalling() throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<RestResult> response = rt.exchange(rootUrl, 
        		HttpMethod.GET, entity, RestResult.class);

		assertEquals(200, response.getStatusCodeValue());

		RestResult rr = response.getBody();
		RestResult.Status status = rr.getStatus();
		assertTrue(status == RestResult.Status.Ok);

		//Jackson mechanism to represent a Generic Type List<Student>
		CollectionType listType = mapper.getTypeFactory()
				.constructCollectionType(List.class, Student.class);
		List<Student> students = mapper.convertValue(rr.getEntity(), listType);
		System.out.println("l2 is " + students);
		
		assertEquals(4, students.size());
	}
	
	/**
	 * Here we test getting the response as Json and then
	 * picking our way through it using the ObjectMapper
	 * We use RestResultGeneric here
	 * @throws IOException
	 */
	@Test
	public void testGetAllWithJsonUsingRestResultGeneric() throws IOException {
		ResponseEntity<String> response = rt.getForEntity(rootUrl, String.class);
		assertEquals(200, response.getStatusCodeValue());
		String raw = response.getBody();
		JsonNode root = mapper.readTree(raw);
		
		Status status = Status.valueOf(root.path("status").asText());
		assertTrue(status == Status.Ok);

		//Have to make this complicated mapping to get
		//ResutResultGeneric<List<Student>>
		CollectionType listType = mapper.getTypeFactory()
				.constructCollectionType(List.class, Student.class);
		JavaType type = mapper.getTypeFactory()
			   .constructParametricType(RestResultGeneric.class, listType);

		//We could unmarshal the whole entity
		RestResultGeneric<List<Student>> rr = mapper.readerFor(type).readValue(root);
		System.out.println("List is " + rr.getEntity());
		
		List<Student> l1 = rr.getEntity();

		// Create the collection type (since it is a collection of Authors)

		//Or we could step through the json to the entity and just unmarshal that
		JsonNode entity = root.path("entity");
		List<Student> l2 = mapper.readerFor(listType).readValue(entity);
		System.out.println("l2 is " + l2);
		
		assertEquals(4, l2.size());

	}

	/**
	 * Here we are using RestResultGeneric having Jackson
	 * do all the unmarshalling and give us the correct object
	 * @throws IOException
	 */
	@Test
	public void testGetAllUsingRestResultGeneric() throws IOException {
		//This is the Spring REST mechanism to create a paramterized type
	    ParameterizedTypeReference<RestResultGeneric<List<Student>>>  
	    		ptr = new ParameterizedTypeReference<RestResultGeneric<List<Student>>> (){};

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<RestResultGeneric<List<Student>>> response = rt.exchange(rootUrl, 
        		HttpMethod.GET, entity, ptr);

		assertEquals(200, response.getStatusCodeValue());
		RestResultGeneric<List<Student>> rr = response.getBody();
		
		Status status = rr.getStatus();
		assertTrue(status == Status.Ok);

		List<Student> l1 = rr.getEntity();
		assertEquals(4, l1.size());
	}

	/**
	 * Here we are using RestResultGeneric having Jackson
	 * do all the unmarshalling and give us the correct object
	 * @throws IOException
	 */
	@Test
	public void testPostOneStudent() throws IOException {
		//This is the Spring REST mechanism to create a paramterized type
		ParameterizedTypeReference<RestResultGeneric<List<Student>>>
				ptr = new ParameterizedTypeReference<RestResultGeneric<List<Student>>> (){};

		Student student = new Student("Curly", "339 03 03030", Student.Status.HIBERNATING);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Student> entity = new HttpEntity<>(student, headers);

		ResponseEntity<RestResult> response = rt.exchange(rootUrl,
				HttpMethod.POST, entity, RestResult.class);

		assertEquals(201, response.getStatusCodeValue());

		RestResult rr = response.getBody();
		RestResult.Status status = rr.getStatus();
		assertTrue(status == RestResult.Status.Ok);

		Student student2 = mapper.convertValue(rr.getEntity(), Student.class);
		assertEquals(5, student2.getId());
	}
}
