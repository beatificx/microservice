package org.beatific.microservice.container.monitor;

import lombok.Data;

@Data
public class ServiceStatus {

	private int numberOfRequest;
	private int transactionPerSeconds;
	private int rateOfFail;
}
