package ttl.larku.controllers.rest;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import ttl.larku.domain.Course;
import ttl.larku.domain.Student;
import ttl.larku.service.CourseService;

@RestController
@RequestMapping("/adminrest/course")
public class CourseRestController {
	
	@Resource
	private CourseService courseService;

	@GetMapping
	public List<Course> getCourses() {
		return courseService.getAllCourses();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCourse(@PathVariable("id") int id) {
		Course c = courseService.getCourse(id);
		if(c == null) {
			return ResponseEntity.badRequest().body(new RestResultGeneric("Course with id: " + id + " not found"));
		}
		return ResponseEntity.ok(new RestResultGeneric<Course>(c));
	}
	

	@GetMapping("/code/{courseCode}")
	public ResponseEntity<?> getCourseByCode(@PathVariable("courseCode") String courseCode) {
		Course c = courseService.getCourseByCode(courseCode);
		if(c == null) {
			return ResponseEntity.badRequest().body(new RestResultGeneric("Course with code: " + courseCode + " not found"));
		}
		return ResponseEntity.ok(new RestResultGeneric<Course>(c));
	}
	
	@PostMapping
	public ResponseEntity<?> addCourse(Course course,
			UriComponentsBuilder ucb) {
		Course newCourse = courseService.createCourse(course);
		UriComponents uriComponents = ucb.path("/course/{id}")
				.buildAndExpand(newCourse.getId());

		return ResponseEntity.created(uriComponents
				.toUri()).body(new RestResultGeneric<Course>(newCourse));
	}
	
	@DeleteMapping("/{id}")
	public void deleteCourse(@PathVariable("id") int id) {
		courseService.deleteCourse(id);
	}
}