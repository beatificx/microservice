package org.beatific.microservice.point;

import javax.servlet.Filter;

import org.beatific.microservice.monitor.service.request.RequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@PropertySource(value = "classpath:META-INF/maven/org.beatific/point-service/pom.properties", ignoreResourceNotFound=true)
//@EnableResourceServer
@ComponentScan(basePackages="org.beatific.microservice")
public class PointServiceApplication {

	@Bean
	public FilterRegistrationBean someFilterRegistration() {
	
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(requestFilter());
	    registration.addUrlPatterns("/*");
	    registration.setName("requestFilter");
	    registration.setOrder(1);
	    return registration;
	} 
	
	@Bean(name = "requestFilter")
	public Filter requestFilter() {
	    return new RequestFilter();
	}
	public static void main(String[] args) {
		SpringApplication.run(PointServiceApplication.class, args);
	}
}
