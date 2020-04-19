package ttl.larku.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ttl.larku.domain.Course;
import ttl.larku.jconfig.LarkUConfig;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath:applicationContext.xml" })
@ContextConfiguration(classes=LarkUConfig.class)
public class CourseServiceTest {

	@Resource
	private CourseService courseService;
	
	@Before
	public void setup() {
		courseService.clear();
	}
	
	@Test
	public void testCreateCourse() {
		Course course = courseService.createCourse("Math-101", "Intro to Math");
		
		Course result = courseService.getCourse(course.getId());
		
		assertTrue(result.getCode().contains("Math-101"));
		assertEquals(1, courseService.getAllCourses().size());
	}
	
	@Test
	public void testDeleteCourse() {
		Course course1= courseService.createCourse("Math-101", "Intro to Math");
		Course course2= courseService.createCourse("Phys-101", "Intro to Physics");
		
		assertEquals(2, courseService.getAllCourses().size());
		
		courseService.deleteCourse(course1.getId());
		
		assertEquals(1, courseService.getAllCourses().size());
		assertTrue(courseService.getAllCourses().get(0).getCode().contains("Phys-101"));
	}

	@Test
	public void testDeleteNonExistentCourse() {
		Course course1= courseService.createCourse("Math-101", "Intro to Math");
		Course course2= courseService.createCourse("Phys-101", "Intro to Physics");
		
		assertEquals(2, courseService.getAllCourses().size());
		
		//Non existent Id
		courseService.deleteCourse(9999);
		
		assertEquals(2, courseService.getAllCourses().size());
	}
	
	@Test
	public void testUpdateCourse() {
		Course course1= courseService.createCourse("Math-101", "Intro to Math");
		
		assertEquals(1, courseService.getAllCourses().size());
		
		course1.setCode("Math-202");
		courseService.updateCourse(course1);
		
		List<Course> courses = courseService.getAllCourses();
		
		assertEquals(1, courses.size());
		assertTrue( courses.get(0).getCode().contains("Math-202"));
	}
}
