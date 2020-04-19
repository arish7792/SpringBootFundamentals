package ttl.larku.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;



@RunWith(MockitoJUnitRunner.Silent.class)
public class StudentServiceUnitTest {

	private String name1 = "Bloke";
	private String name2 = "Blokess";
	private String newName = "Karl Jung";
	private String phoneNumber1 = "290 298 4790";
	private String phoneNumber2 = "3838 939 93939";

	@InjectMocks
	private StudentService studentService;

	@Mock
	private BaseDAO<Student> studentDAO;
	
	@Mock
	private List<Student> allStudents;
	
	@Before
	public void setup() {
		studentService.clear();
	}
	
	@Test
	public void testCreateStudent() {
		Student s = new Student(name1, phoneNumber1, Status.FULL_TIME);
		s.setId(1);

		Mockito.when(studentDAO.create(s)).thenReturn(s);
		Mockito.when(studentDAO.get(1)).thenReturn(s);
		Mockito.when(studentDAO.getAll()).thenReturn(allStudents);
		Mockito.when(allStudents.size()).thenReturn(1);

		Student newStudent = studentService.createStudent(name1, phoneNumber1, Status.FULL_TIME);
		
		Student result = studentService.getStudent(newStudent.getId());
		
		assertTrue(result.getName().contains(name1));
		assertEquals(1, studentService.getAllStudents().size());
	}
	
	@Test
	public void testDeleteStudent() {
		Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student1.setId(1);
		Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2.setId(2);

		Mockito.when(studentDAO.create(student1)).thenReturn(student1);
		Mockito.when(studentDAO.create(student2)).thenReturn(student2);
		Mockito.when(studentDAO.get(1)).thenReturn(student1);

		//different style
		Mockito.doReturn(allStudents).when(studentDAO).getAll();

		Mockito.when(allStudents.size()).thenReturn(2).thenReturn(1);
		Mockito.when(allStudents.get(0)).thenReturn(student2);

		student1 = studentService.createStudent(student1);
		student2 = studentService.createStudent(student2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		studentService.deleteStudent(student1.getId());
		
		assertEquals(1, studentService.getAllStudents().size());
		assertTrue(studentService.getAllStudents().get(0).getName().contains(name1));
	}

	@Test
	public void testDeleteNonExistentStudent() {
		Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student1.setId(1);
		Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2.setId(2);
		Mockito.when(studentDAO.create(student1)).thenReturn(student1);
		Mockito.when(studentDAO.create(student2)).thenReturn(student2);
		Mockito.when(studentDAO.get(1)).thenReturn(student1);

		//different style
		Mockito.doReturn(allStudents).when(studentDAO).getAll();

		Mockito.when(allStudents.size()).thenReturn(2).thenReturn(2);
		Mockito.when(allStudents.get(0)).thenReturn(student2);

		student1 = studentService.createStudent(student1);
		student2 = studentService.createStudent(student2);
		
		assertEquals(2, studentService.getAllStudents().size());
		
		//Non existent Id
		studentService.deleteStudent(9999);
		
		assertEquals(2, studentService.getAllStudents().size());
		
	}
	
	@Test
	public void testUpdateStudent() {
		Student student1 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student1.setId(1);
		Student student2 = new Student(name1, phoneNumber1, Status.FULL_TIME);
		student2.setId(2);

		Mockito.when(studentDAO.create(student1)).thenReturn(student1);
		Mockito.when(studentDAO.create(student2)).thenReturn(student2);
		Mockito.when(studentDAO.get(1)).thenReturn(student1);

		//different style
		Mockito.doReturn(allStudents).when(studentDAO).getAll();

		Mockito.when(allStudents.size()).thenReturn(1).thenReturn(1);
		Mockito.when(allStudents.get(0)).thenReturn(student2);

		student1 = studentService.createStudent(student1);
		
		assertEquals(1, studentService.getAllStudents().size());
		
		student1.setName(name2);
		studentService.updateStudent(student1);
		
		assertEquals(1, studentService.getAllStudents().size());
		assertTrue( studentService.getAllStudents().get(0).getName().contains(name2));

	}
}
