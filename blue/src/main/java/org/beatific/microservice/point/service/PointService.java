package org.beatific.microservice.point.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
public class PointService {
	
	@RequestMapping("/get")
    public Point get() {
		return new Point(1L, "name", 1000L);
    }
}
