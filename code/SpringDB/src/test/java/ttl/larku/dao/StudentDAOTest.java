package ttl.larku.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import ttl.larku.domain.Student;

@RunWith(SpringRunner.class)
@SpringBootTest

//Populate your DB.  You can either do this or the schema[-XXX].sql and data[-XXX].sql files
//and @DirtiesContext as below
//@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql", "/ttl/larku/db/populateDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//This will make recreate the context after every test.  
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentDAOTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	private Student student1;
	private Student student2;
	
	@Resource(name="studentDAO")
	private BaseDAO<Student> dao;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
	}
	
	
	@Test
	//@DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
	public void testGetAll() {
		List<Student> students = dao.getAll();
		assertEquals(4, students.size());
	}
	
	@Test
	public void testCreate() {
		
		int newId = dao.create(student1).getId();
		
		Student resultstudent = dao.get(newId);
		
		assertEquals(newId, resultstudent.getId());
	}
	
	@Test
	public void testUpdate() {
		int newId = dao.create(student1).getId();
		
		Student resultStudent = dao.get(newId);
		
		assertEquals(newId, resultStudent.getId());
		
		resultStudent.setName(newName);
		dao.update(resultStudent);
		
		resultStudent = dao.get(resultStudent.getId());
		assertEquals(newName, resultStudent.getName());
	}
	
	@Test
	public void testDelete() {
		int id1 = dao.create(student1).getId();
		
		Student resultStudent = dao.get(id1);
		assertEquals(resultStudent.getId(), id1);
		
		dao.delete(resultStudent);
		
		resultStudent = dao.get(id1);
		
		assertEquals(null, resultStudent);
		
	}

}
