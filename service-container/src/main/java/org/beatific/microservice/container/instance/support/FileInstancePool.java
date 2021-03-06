package org.beatific.microservice.container.instance.support;

import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.repository.FileObjectRepository;
import org.springframework.stereotype.Component;

@Component
public class FileInstancePool extends InstancePool {

	private static String filename = "c:/microservice/instance.file";
	
	public FileInstancePool() {
		super.repository = new FileObjectRepository<>(filename);
	}
}
