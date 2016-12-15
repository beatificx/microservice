package org.beatific.microservice.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
//@EnableResourceServer
public class ServiceContainerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceContainerApplication.class, args);
	}
}
