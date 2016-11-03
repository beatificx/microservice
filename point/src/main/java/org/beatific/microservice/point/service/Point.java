package org.beatific.microservice.point.service;

import org.springframework.stereotype.Component;

@Component
public class Point {

	private int point = 10000;
	
	public Point redeem(int point) {
		this.point -= point;
		return this;
	}
}
