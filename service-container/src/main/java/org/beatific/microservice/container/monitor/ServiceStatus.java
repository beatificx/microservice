package org.beatific.microservice.container.monitor;

import java.util.List;

import lombok.Data;

@Data
public class ServiceStatus {

	private String serviceName;
	private int request;
	private int success;
	private int fail;
	
	private List<InstanceStatus> instanceStatus;
}
