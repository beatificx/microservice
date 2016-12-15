package org.beatific.microservice.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@PropertySource(value = "classpath:META-INF/maven/org.beatific/point-service/pom.properties", ignoreResourceNotFound=true)
//@EnableResourceServer
@ComponentScan(basePackages="org.beatific.microservice")
public class PointServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PointServiceApplication.class, args);
	}
}
