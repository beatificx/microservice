package org.beatific.microservice.container.service.support;

import java.util.Map;

import org.beatific.microservice.container.repository.FileObjectRepository;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.service.ServiceContainer;

public class FileServiceContainer extends ServiceContainer {
	
	private final String containerFilename = "c:/microservice/service.file";
	
	public FileServiceContainer() {
		super.repository = new FileObjectRepository<Map<String, Service>>(containerFilename);
	}

}
