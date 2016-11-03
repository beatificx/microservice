package org.beatific.microservice.point;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"org.beatific.microservice.point"})
public class PointServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PointServiceApplication.class, args);
	}
}
