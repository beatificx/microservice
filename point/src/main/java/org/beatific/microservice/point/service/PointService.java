package org.beatific.microservice.point.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@RestController
public class PointService {
	
	@Autowired
	private Point point;

	@RequestMapping("/point/{point}")
    public Point reduce(@PathVariable int point) {
		return this.point.redeem(point);
    }
}
