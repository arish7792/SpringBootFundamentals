package ttl.larku.dao.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.Course;

import java.util.List;

/**
 * A custom repository that builds on the base interface
 * 
 * Look at ttl.larku.dao.CustomRepoTest for usage examples
 * 
 * @author whynot
 *
 */
@Repository
@Transactional
//Uncomment next line to NOT expose this repo as a REST resource
//@RepositoryRestResource(exported = false)
public interface CustomRepo extends CustomRepoBase<Course, Integer>{
	
	public List<Course> findByCode(@Param("code") String code);
}
