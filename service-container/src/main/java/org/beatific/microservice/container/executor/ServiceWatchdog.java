package org.beatific.microservice.container.executor;

import java.util.stream.IntStream;

import org.beatific.microservice.container.instance.InstancePhysicalManager;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.monitor.ServiceMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceWatchdog {

	@Autowired
	private ServiceMonitor monitor;

	@Autowired
	private ServiceStatusResolver resolver;

	@Autowired
	private InstancePool instancePool;
	
	@Autowired
	private InstancePhysicalManager physicalManager;
	
	@Autowired
	private Judge judge;

	public void watch() {

		instancePool.instancesByServiceName().forEach((name, instances) -> {

			log.debug("name [{}], instances {}", name, instances);
			
			int count = resolver.resolve(name, monitor.monitor(instances));
			
			log.debug("count [{}]", count);
			
			if(instances.size() + count > 0 ) { 
			
				if(count > 0) 
					IntStream.range(0, count).forEach(i -> {
						physicalManager.invoke(name);
					});
				
				else if(count < 0)
				    judge.cooldown(monitor.reduceInstances(name, count * -1));
			}

		});

	}

}
