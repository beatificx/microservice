package org.beatific.microservice.container.service;

import lombok.Data;

@Data
public class Instance {

	private String serviceName;
	private String hostname;
	private String port;
}
