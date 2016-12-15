package org.beatific.microservice.point.service;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {

	@Id
	private final Long id;
	private String name;
	private Long point;

}
