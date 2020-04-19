package ttl.larku.controllers.rest;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import ttl.larku.domain.Student;
import ttl.larku.service.StudentService;

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

    @GetMapping(value = "/headers/{id}", headers = {"someheader=somevalue"})
    public ResponseEntity<?> getStudentWithHeader(@PathVariable("id") int id) {
        Student s = studentService.getStudent(id);
        if (s == null) {
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
	public ResponseEntity<?> updateStudent(Student student) {
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











//    @PostMapping
//    public ResponseEntity<?> createStudent(@RequestBody Student s,
//                                           UriComponentsBuilder ucb, Errors errors) {
////        validator.validate(s, errors);
//        if (errors.hasErrors()) {
//            List<String> errmsgs = errors.getFieldErrors().stream()
//                    .map(error -> "error:" + error.getField() + ": " + error.getDefaultMessage()
//                            + ", supplied Value: ")
//                    .collect(toList());
//            return ResponseEntity.badRequest().body(new RestResult(RestResult.Status.Error,
//                    errmsgs));
//        }
//        s = studentService.createStudent(s);
//        UriComponents uriComponents = ucb.path("/student/{id}")
//                .buildAndExpand(s.getId());
//
//        return ResponseEntity.created(uriComponents.toUri()).body(new RestResult(s));
//    }
