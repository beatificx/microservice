package org.beatific.microservice.monitor.service.instance;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InstanceStatus {

	@NonNull
	private Instance instance;
	
	private int request;
	private int success;
	private int fail;
	
	private long totoalMemory;
	private long usedMemory;
	private long freeMemory;
	private long maxMemory;
}
