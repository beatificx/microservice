package org.beatific.microservice.container.context;

import javax.annotation.PostConstruct;

import org.beatific.microservice.container.service.ServiceLoader;
import org.springframework.stereotype.Component;

@Component
public class ServiceContext extends ServiceLoader {

	@PostConstruct
	public void init() {
		load();
	}
}
