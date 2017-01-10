package org.beatific.microservice.container.service;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Service implements Serializable {

	private String serviceName;
	private String startCommand;
	private Level level;
	private List<String> parents;
	private List<String> children;
}
