package org.beatific.microservice.point.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.UUID;
import java.util.stream.IntStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
public class PointService {
	
//	@HystrixCommand
	@RequestMapping("/")
    public Point get() {
		IntStream.range(1, 30000).mapToObj((i) -> UUID.randomUUID().toString()).reduce((a, b) -> a.concat(b)).get();
		return new Point(1L, "name", 1000L);
    }
}
