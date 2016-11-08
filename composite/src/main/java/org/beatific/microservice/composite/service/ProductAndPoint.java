package org.beatific.microservice.composite.service;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAndPoint {

	private Long productId;
	private String productName;
	private Long price;
	
	private Long pointId;
	private String userName;
	private Long point;
}
