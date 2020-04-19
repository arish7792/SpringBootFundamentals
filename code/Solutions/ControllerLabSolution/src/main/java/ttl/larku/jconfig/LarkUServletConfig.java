package ttl.larku.jconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
@EnableWebMvc  
//TODO - Add appropriate Package Definitions here
@ComponentScan(basePackages={"ttl.larku.controllers"})
//TODO - Profile needs to be set correctly
@Profile({"development", "production"})
public class LarkUServletConfig implements WebMvcConfigurer {

	@Bean 
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
		rbms.setBasename("larkU");

		return rbms;
		
	}

	
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(new Locale("en", "US"));
		//slr.setDefaultLocale(Locale.getDefault());
		
		return slr;
	}
	
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean lvfb = new LocalValidatorFactoryBean();
		lvfb.setValidationMessageSource(messageSource());
		
		return lvfb;
	}
	
/*	
	@Bean
	public JAXBContext getJaxbContext() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance("ttl.larku.domain");
		return context;
	}
*/	
	/**
	 * Deal with static resources in the "normal" way
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
