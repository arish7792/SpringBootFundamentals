package ttl.larku;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryCourseDAO;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.jpahibernate.JPACourseDAO;
import ttl.larku.dao.jpahibernate.JPAStudentDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.Student;
import ttl.larku.service.CourseService;

@Configuration
@PropertySource({"classpath:/larkUContext.properties"})
public class LarkUConfig {
	
	@Autowired
	private Environment env;
	
	private LarkUTestDataConfig testDataProducer = new LarkUTestDataConfig();
	
	@Value("${larku.profile.active}")
	private String profile;

	@Bean
	@Profile("development")
	public BaseDAO<Student> studentDAO() {
//		BaseDAO<Student> bs = testDataProducer.studentDAOWithInitData();
//		return bs;
        return jpaStudentDAO();
	}

	@Bean(name="studentDAO")
	@Profile("production")
	public BaseDAO<Student> studentDAOJpa() {
			return jpaStudentDAO();
	}

	@Bean
	@Profile("development")
	public BaseDAO<Course> courseDAO() {
		//return inMemoryCourseDAO();
		//return testDataProducer.courseDAOWithInitData();
			return jpaCourseDAO();
	}

	@Bean(name="courseDAO")
	@Profile("production")
	public BaseDAO<Course> courseDAOJPA() {
			return jpaCourseDAO();
	}

	@Bean
	public BaseDAO<Student> inMemoryStudentDAO() {
		return new InMemoryStudentDAO();
	}

	@Bean
	public BaseDAO<Student> jpaStudentDAO() {
		return new JPAStudentDAO();
	}


	@Bean
	public CourseService courseService() {
		CourseService cc = new CourseService();
		cc.setCourseDAO(courseDAO());
		
		return cc;
	}


	@Bean 
	public BaseDAO<Course> inMemoryCourseDAO() {
		return new InMemoryCourseDAO();
	}

	@Bean 
	public BaseDAO<Course> jpaCourseDAO() {
		return new JPACourseDAO();
	}
	
}