package ttl.larku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryTrackDAO;
import ttl.larku.dao.jpahibernate.JPATrackDAO;
import ttl.larku.domain.Track;
import ttl.larku.service.TrackService;

import java.util.List;

@Configuration
@ComponentScan({ "ttl.larku.service", "ttl.larku.dao" })
@PropertySource({ "classpath:/larkUContext.properties" })
public class TrackerConfig {

	@Autowired
	private Environment env;

	private TrackerTestDataConfig testDataProducer = new TrackerTestDataConfig();

	@Value("${larku.profile.active}")
	private String profile;

	@Bean
	@Profile("development")
	public BaseDAO<Track> trackDAO() {
		// return inMemoryStudentDAO();
		return testDataProducer.trackDAOWithInitData();
	}

	@Bean(name = "trackDAO")
	@Profile("production")
	public BaseDAO<Track> trackDAOJpa() {
		//return jpaTrackDAO();
		return jpaTrackDAOWithTestData();
	}


	@Bean
	public BaseDAO<Track> inMemoryTrackDAO() {
		return new InMemoryTrackDAO();
	}

	@Bean
	public BaseDAO<Track> jpaTrackDAOWithTestData() {
		JPATrackDAO dao = new JPATrackDAO();
		List<Track> fakeTracks = testDataProducer.tracks();
		fakeTracks.forEach(dao::create);
		
		return dao;
	}

	@Bean
	public BaseDAO<Track> jpaTrackDAO() {
		JPATrackDAO dao = new JPATrackDAO();
		return dao;
	}

	@Bean
	public TrackService trackService() {
		TrackService ss = new TrackService();
		ss.setTrackDAO(trackDAO());

		return ss;
	}
}