package org.beatific.microservice.container.instance;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InstanceFinder {

	@Autowired
	private LoadBalancerClient loadBalancer;
	@Autowired
	private DiscoveryClient discoveryClient;
	@Autowired
	private EurekaClient eurekaClient;

	public URI getServiceUrl(String serviceName) {
		return getServiceInstance(serviceName).getUri();
	}

	public ServiceInstance getServiceInstance(String serviceName) {
		ServiceInstance instance = loadBalancer.choose(serviceName);

		if (instance == null) {
			log.error("Can't find a service with serviceName = {}", serviceName);
		}

		return instance;
	}

	public List<ServiceInstance> getAllInstaces(String serviceName) {
		return discoveryClient.getInstances(serviceName);
	}

	public Instance getInstaceInfo(String serviceName) {
		InstanceInfo info = eurekaClient.getNextServerFromEureka(serviceName.toUpperCase(), false);

		if (info == null) return null;

		return  new Instance(info);
	}

	public List<Instance> getAllInstaceInfos(String serviceName) {
		Application application = eurekaClient.getApplication(serviceName.toUpperCase());

		if (application == null)
			return new ArrayList<>();

		 List<InstanceInfo> infos = application.getInstances();

		 List<Instance> instances = new ArrayList<>();

		 infos.forEach(info -> instances.add(new Instance(info)));

		return instances;
	}

	public List<String> getServiceUrls(String serviceName) {

		return discoveryClient.getInstances(serviceName).stream().map(instance -> instance.getUri().toString()).collect(Collectors.toList());

	}

}
