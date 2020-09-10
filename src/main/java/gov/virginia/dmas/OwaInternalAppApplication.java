package gov.virginia.dmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class OwaInternalAppApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(OwaInternalAppApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	   return application.sources(OwaInternalAppApplication.class);
	}
}

