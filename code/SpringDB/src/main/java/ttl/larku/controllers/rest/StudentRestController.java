package ttl.larku.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ttl.larku.domain.RestResult;
import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/adminrest/student")
public class StudentRestController {
	
	@Resource
	private StudentService studentService;

	@GetMapping
	public ResponseEntity<?> getAllStudents() {
		List<Student> students = studentService.getAllStudents();
		return ResponseEntity.ok(new RestResult().entity(students));
	}
	
	@PostMapping
	public ResponseEntity<?> createStudent(@RequestBody Student s, 
			UriComponentsBuilder ucb) {
		s = studentService.createStudent(s);
		UriComponents uriComponents = ucb.path("/student/{id}")
				.buildAndExpand(s.getId());

		return ResponseEntity.created(uriComponents.toUri()).body(new RestResult(s));
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getStudent(@PathVariable("id") int id) {
		Student s = studentService.getStudent(id);
		if(s == null) {
			return ResponseEntity.badRequest().body(new RestResult(RestResult.Status.Error,
					"Student with id: " + id + " not found"));
		}
		return ResponseEntity.ok(new RestResult(s));
	}

	@GetMapping("/byname/{name}")
	public ResponseEntity<?> getStudentByName(@PathVariable("name") String name) {
		List<Student> result = studentService.getByName(name);
		return ResponseEntity.ok(new RestResult().entity(result));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable("id") int id) {
		Student s = studentService.getStudent(id);
		if(s == null) {
			RestResult rr = new RestResult("Student with id " + id + " not found");
			return ResponseEntity.badRequest().body(rr);
		}
		studentService.deleteStudent(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping
	public ResponseEntity<?> updateStudent(@RequestBody Student student) {
		int id = student.getId();
		Student s = studentService.getStudent(id);
		if(s == null) {
			RestResult rr = new RestResult("Student with id " + id + " not found");
			return ResponseEntity.badRequest().body(rr);
		}
		studentService.updateStudent(student);
		return ResponseEntity.noContent().build();
	}
}
