package org.beatific.microservice.container.instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class DiscoveryRegistry {

	private static final String UNREGISTRY_URL = "/blueGreen";
	private static final String TAGGING_URL = "/coolDown";
	
	@Value("${spring.application.name}")
	private String self;
	
	@Autowired
	private InstanceFinder finder;
	private InstanceCaster<Boolean> caster = new InstanceCaster<>(Boolean.class);	
	private Map<String, List<String>> serviceUrls = new HashMap<String, List<String>>();
	
	public synchronized void tagServices(String serviceName) {
		log.debug("tag service[{}]", serviceName);
		serviceUrls.clear();
		serviceUrls.put(serviceName, finder.getServiceUrls(serviceName));
		caster.multiCast(self, TAGGING_URL, serviceUrls.get(serviceName));
	}
	
	public synchronized List<String> unregister(String serviceName) {
		log.debug("unregister service[{}]", serviceName);
		List<String> urls = serviceUrls.get(serviceName);
		caster.multiCast(urls, UNREGISTRY_URL);
		return urls;
	}
}
