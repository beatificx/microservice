package org.beatific.microservice.container.monitor;

import java.util.List;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.machine.Machine;
import org.beatific.microservice.container.service.Service;

public interface ServiceMonitor {

	public ServiceStatus monitor(Service service);
	
	public ServiceStatus monitor(Instance instance);
	
	public ServiceStatus monitor(List<Instance> instance);
	
	public ServiceStatus monitor(Machine instance);
}
