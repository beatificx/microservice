package org.beatific.microservice.composite.service;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	private Long id;
	private String name;
	private Long price;

}
