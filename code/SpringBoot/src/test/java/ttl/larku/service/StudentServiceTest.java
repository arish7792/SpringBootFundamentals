package ttl.larku.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentServiceTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Karl Jung";
	private String phoneNumber1 = "290 298 4790";
	private String phoneNumber2 = "3838 939 93939";
	
	@Resource
	private StudentService studentService;
	
	@Before
	public void setup() {
		//studentService = new StudentService();
		//studentService = applicationContext.getBean("studentService", StudentService.class);
		studentService.clear();
	}
	
	@Test
	public void testCreateStudent() {
		Student newStudent = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);
		
		Student result = studentService.getStudent(newStudent.getId());
		
		assertTrue(result.getName().contains(name1));
		assertEquals(1, studentService.getAllStudents().size());
	}
	
	@Test
	public void testDeleteStudent() {
		Student student1 = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);
		Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2 = studentService.createStudent(student2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		studentService.deleteStudent(student1.getId());
		
		assertEquals(1, studentService.getAllStudents().size());
		assertTrue(studentService.getAllStudents().get(0).getName().contains(name1));
	}

	@Test
	public void testDeleteNonExistentStudent() {
		Student student1 = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);
		Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2 = studentService.createStudent(student2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		//Non existent Id
		studentService.deleteStudent(9999);
		
		assertEquals(2, studentService.getAllStudents().size());
	}
	
	@Test
	public void testUpdateStudent() {
		Student student1 = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);
		
		assertEquals(1, studentService.getAllStudents().size());
		
		student1.setName(name2);
		studentService.updateStudent(student1);
		
		assertEquals(1, studentService.getAllStudents().size());
		assertTrue( studentService.getAllStudents().get(0).getName().contains(name2));
	}
}
