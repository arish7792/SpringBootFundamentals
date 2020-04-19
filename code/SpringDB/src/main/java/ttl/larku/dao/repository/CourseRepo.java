package ttl.larku.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ttl.larku.domain.Student;

import java.util.List;

//@Repository
//@Transactional
//Uncomment next line to not expose this repo as a REST resource
//@RepositoryRestResource(exported = false)
public interface CourseRepo { //extends JpaRepository<Course, Integer>{

	@Query("select c from Course c where c.code = :code")
	public List<Student> findByCode(@Param("code") String code);
}
