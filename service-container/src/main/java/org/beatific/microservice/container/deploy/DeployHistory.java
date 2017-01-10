package org.beatific.microservice.container.deploy;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DeployHistory implements Serializable {

	private String version;
	private long timeStamp;
	
}
