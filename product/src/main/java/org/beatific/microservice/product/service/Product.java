package org.beatific.microservice.product.service;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {

	@Id
	private final Long id;
	private String name;
	private Long price;

}
