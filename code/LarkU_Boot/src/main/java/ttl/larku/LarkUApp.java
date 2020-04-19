package ttl.larku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@Import({LarkUTestDataConfig.class, LarkURealDataConfig.class})
@EnableJpaRepositories
@EnableTransactionManagement
public class LarkUApp extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LarkUApp.class, args);
	}
}
