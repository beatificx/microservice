package org.beatific.microservice.container.deploy;

import org.beatific.microservice.container.environment.Server;

import lombok.Data;

@Data
public class Deployment {

	private String version;
	private DeployType type;
	private String serviceName;
	private Server server;
}
