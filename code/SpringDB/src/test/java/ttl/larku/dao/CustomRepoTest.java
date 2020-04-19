package ttl.larku.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.dao.repository.CustomRepo;
import ttl.larku.domain.Course;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql",
		"/ttl/larku/db/populateDB-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class CustomRepoTest {
	
	@Autowired
	private CustomRepo customRepo;

	@Test
	public void testFindAll() {
		List<Course> courses = customRepo.findAll();
		
		assertEquals(3, courses.size());
	}

	@Test
	public void testOptional() {
		Optional<Course> oCourse = customRepo.findById(1);
		
		
		assertNotNull(oCourse.orElse(null));
		
		//test for null optional
		oCourse = customRepo.findById(1000);
		
		assertNull(oCourse.orElse(null));
	}
	
	@Test
	public void testForNullReturn() {
		Course course = customRepo.findNullableById(1);
		assertNotNull(course);

		course = customRepo.findNullableById(1000);
		assertNull(course);
	}

	/**
	 * The is testing the absence of @Nullable in the interface.
	 * For this test to succeed a package-info.java file has to exist
	 * with the following lines in it:
	 * 
	 * @org.springframework.lang.NonNullApi
     * package ttl.larku.dao.repository; 
     * 
	 */
	@Test(expected = EmptyResultDataAccessException.class)
	public void testForException() {
		Course course = customRepo.findExceptionById(1000);
		assertNull(course);
	}

	@Test
	public void testForNoException() {
		Course course = customRepo.findExceptionById(1);
		assertNotNull(course);
	}
}
