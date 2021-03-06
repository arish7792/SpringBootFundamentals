package ttl.larku.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ttl.larku.MyProfileValueSource;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest

//Use this to conditionally include a test.  Look at MyProfileValueSource.java.
@ProfileValueSourceConfiguration(MyProfileValueSource.class)
@IfProfileValue(name = "spring.profiles.active", values = {"development"})

//Populate your DB.  You can either do this or use the schema[-XXX].sql and data[-XXX].sql files
//and @DirtiesContext as below
//@Sql(scripts = { "/ttl/larku/db/createDB-h2.sql", "/ttl/larku/db/populateDB-h2.sql" }, executionPhase=ExecutionPhase.BEFORE_TEST_METHOD)

//This will recreate the context after every test.
//In conjunction with appropriate 'schema[-XXX].sql' and 'data[-XXX].sql' files
//it will also drop and recreate the DB before each test.
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class StudentServiceTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Different Bloke";
	
	private String code1 = "MATH-101";
	private String code2 = "BOT-202";
	private String badCode = "BADNESS";
	private String title1 = "Intro To Botany";
	private String title2 = "Outtro To Botany";
	
	private String startDate1 = "10/10/2012";
	private String startDate2 = "10/10/2012";
	private String endDate1 = "08/10/2013";
	private String endDate2 = "10/10/2013";
	
	private String badStartDate = "12/12/2099";
	
	private Course course1;
	private Course course2;
	private ScheduledClass class1;
	private ScheduledClass class2;
	private Student student1;
	private Student student2;
	
	@Resource
	private StudentService studentService;
	
	@Before
	public void setup() {
		student1 = new Student(name1);
		student2 = new Student(name2);
		course1 = new Course(code1, title1);
		course2 = new Course(code2, title2);
		
		class1 = new ScheduledClass(course1, startDate1, endDate1);
		class2 = new ScheduledClass(course2, startDate2, endDate2);
		
	}
	
	@Test
	public void testCreateStudent() {
		Student newStudent = studentService.createStudent(name1);
		
		Student result = studentService.getStudent(newStudent.getId());
		
		assertEquals(name1, result.getName());
		assertEquals(5, studentService.getAllStudents().size());
	}
	
	@Test
	public void testDeleteStudent() {
		Student student1 = studentService.createStudent(name1);
		Student student2 = studentService.createStudent(name2);
		
		assertEquals(6, studentService.getAllStudents().size());
		
		studentService.deleteStudent(student1.getId());
		
		assertEquals(5, studentService.getAllStudents().size());
	}

	@Test
	public void testDeleteNonExistentStudent() {
		Student student1 = studentService.createStudent(name1);
		Student student2 = studentService.createStudent(name2);
		
		assertEquals(6, studentService.getAllStudents().size());
		
		//Non existent Id
		studentService.deleteStudent(9999);
		
		assertEquals(6, studentService.getAllStudents().size());
	}
	
	@Test
	public void testUpdateStudent() {
		Student student1 = studentService.createStudent(name1);
		
		assertEquals(5, studentService.getAllStudents().size());
		
		student1.setName(name2);
		studentService.updateStudent(student1);
		
		assertEquals(5, studentService.getAllStudents().size());
		assertEquals(name2, studentService.getStudent(student1.getId()).getName());
	}
}