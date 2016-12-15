package org.beatific.microservice.container.service.support;

import java.net.URI;
import java.util.List;

import org.beatific.microservice.container.service.Instance;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.service.ServiceContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EurekaServiceContainer extends ServiceContainer {

	private static final String SERVICE_CONTAINER = "service-container";
	
	@Autowired
    private LoadBalancerClient loadBalancer;
	
	private URI getServiceUrl(String serviceName) {
        URI uri = null;
        ServiceInstance instance = loadBalancer.choose(serviceName);

        if (instance == null) {
        	log.debug("Can't find a service with serviceName = {}" , serviceName);
            throw new RuntimeException("Can't find a service with serviceName = " + serviceName);
        }

        uri = instance.getUri();
        return uri;
    }
	
	@Override
	public void registerServic(String serviceName, Service service) {
		URI uri = getServiceUrl(SERVICE_CONTAINER);
				
	}

	@Override
	public Service getService(String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Instance> getAllInstaces(String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void clearServices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void reloadServices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeService(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void play() {
		// TODO Auto-generated method stub
		
	}

}
