package ttl.larku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttl.larku.dao.repository.StudentRepo;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentRepoService {

	@Autowired
	private StudentRepo studentDAO;

	public Student createStudent(String name) {
		Student student = new Student(name);
		student = studentDAO.save(student);

		return student;
	}

	public Student createStudent(String name, String phoneNumber, Status status) {
		return createStudent(new Student(name, phoneNumber, status));
	}

	public Student createStudent(Student student) {
		student = studentDAO.save(student);

		return student;
	}

	public void deleteStudent(int id) {
		studentDAO.findById(id).ifPresent(student -> {
			studentDAO.delete(student);
		});
	}

	public void updateStudent(Student student) {
		studentDAO.save(student);
	}

	public Student getStudent(int id) {
		return studentDAO.findById(id).orElse(null);
	}

	public List<Student> getAllStudents() {
		return studentDAO.findAll();
	}

	public List<Student> getByName(String name) {
		String lc = name.toLowerCase();
		List<Student> result = getAllStudents().stream().filter(s -> s.getName().toLowerCase().contains(lc))
				.collect(Collectors.toList());
		return result;
	}

	public StudentRepo getStudentDAO() {
		return studentDAO;
	}

	public void setStudentDAO(StudentRepo studentDAO) {
		this.studentDAO = studentDAO;
	}

	public void clear() {
	}

}
