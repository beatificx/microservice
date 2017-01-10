package org.beatific.microservice.healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class HealthCheckApplication extends WebMvcConfigurerAdapter {

//	@Autowired(required = false)
//	private DataSource dataSource;
//
//	@Bean
//	@Primary
//	public DataSourceHealthIndicator dataSourceHealthIndicator() {
//	    return new DataSourceHealthIndicator(dataSource, "SELECT 1 FROM DUAL");
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(HealthCheckApplication.class, args);
	}

}

