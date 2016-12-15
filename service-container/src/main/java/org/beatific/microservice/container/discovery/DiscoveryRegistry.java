package org.beatific.microservice.container.discovery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class DiscoveryRegistry {

	private static final String UNREGISTRY_URL = "/blueGreen";
	
	private DiscoveryExecutor<Void> executor = new DiscoveryExecutor<Void>(Void.class);
	
	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private DiscoveryClient discoveryClient;
	
	@PostConstruct
	public void init() {
		executor.setDiscoveryClient(discoveryClient);
		executor.setLoadBalancer(loadBalancer);
	}
	
	private Map<String, List<String>> serviceUrls = new HashMap<String, List<String>>();
	
	public void tagServices(String serviceName) {
		log.debug("servicename {}", serviceName);
		serviceUrls.put(serviceName, executor.getServiceUrls(serviceName));
	}
	public void unregister(String serviceName) {
		executor.multiExecute(serviceUrls.get(serviceName), UNREGISTRY_URL);
	}
}
