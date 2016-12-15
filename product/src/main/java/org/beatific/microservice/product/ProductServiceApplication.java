package org.beatific.microservice.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
//@EnableResourceServer
@ComponentScan(basePackages="org.beatific.microservice")
@PropertySource(value = "classpath:META-INF/maven/org.beatific/product-service/pom.properties", ignoreResourceNotFound=true)
public class ProductServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
