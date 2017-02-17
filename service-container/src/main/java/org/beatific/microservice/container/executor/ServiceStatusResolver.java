package org.beatific.microservice.container.executor;

import java.util.HashMap;
import java.util.Map;

import org.beatific.microservice.container.monitor.ServiceStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServiceStatusResolver {

	private Map<String, ServiceStatistics> statistics = new HashMap<>();
	
	public int resolve(String name, ServiceStatus status) {
		ServiceStatistics stats = statistics.get(name);
		if(stats == null) stats = new ServiceStatistics();
		log.debug("statistics {}, status {}", stats, status);
		return stats.collect(status.getRequest(), status.getSuccess(), status.getFail(), status.getInstanceStatus());
	}
}
