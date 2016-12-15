package org.beatific.microservice.eureka.unregistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.EurekaClient;

@RestController
public class EurekaUnregistry {

	@Autowired
	private EurekaClient client;
	
	@RequestMapping("/blueGreen")
	public String unregister() {
		client.shutdown();
		return "shutdown";
	}
}
