package org.beatific.microservice.container.executor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.repository.RepositoryException;
import org.beatific.microservice.container.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AtLeastOneWatchdog implements Watchdog {

	@Autowired
	ApplicationInfoManager manager;
	
	@Override
	public void watch(Map<String, Service> servicePool, InstancePool instancePool, InstanceFinder finder, Executor executor) {
		
		InstanceInfo info = manager.getInfo();
		log.error("instanceid [{}]", info.getInstanceId());
		log.error("appname [{}]", info.getAppName());
		log.error("id [{}]", info.getId());
		
		Instance instance = null;

		for (Service service : servicePool.values()) {
			log.info("service {}", service);
			List<ServiceInstance> serviceInstances = finder.getAllInstaces(service.getServiceName());
			log.info("service name [{}], instances [{}], instance size [{}]", service.getServiceName(), serviceInstances,
					serviceInstances.size());
			if (serviceInstances.size() == 0) {
				try {
					int pid = executor.execute(service.getStartCommand());
					List<Instance> instances = new ArrayList<>();;
					while (instances.size() == 0) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						instances = finder.getAllInstaceInfos(service.getServiceName());
						log.error("instances {}", instances);
						
					}
					
					if(instances.size() == 1) {
						log.error("search instances {}", instances);
						instance = instances.get(0);
						
					} else {
						
						for(Instance ins : instances) {
							if(instancePool.get(ins.getUri()) == null) {
								instance = ins;
								break;
							}
						}
					}

					instance.setService(service);
					instance.setPid(pid);

					instancePool.register(instance);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (RepositoryException e) {
					// kill instance
				}
			}
		}

	}

}
