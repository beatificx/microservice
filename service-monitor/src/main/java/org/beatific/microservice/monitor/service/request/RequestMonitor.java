package org.beatific.microservice.monitor.service.request;

import java.util.List;

import org.beatific.microservice.monitor.service.instance.Instance;
import org.beatific.microservice.monitor.service.instance.InstanceFactory;
import org.beatific.microservice.monitor.service.instance.InstanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestMonitor {

	@Autowired 
	private InstanceFactory factory;
	
	@Autowired
	private RequestHolder holder;
	
	Runtime runtime = Runtime.getRuntime();
	
	@RequestMapping("/instance")
	public Instance instance() {
		
		return factory.getInstance();
	}
	
	@RequestMapping("/monitorRequest")
	public InstanceStatus monitor() {
		
		InstanceStatus status = new InstanceStatus(factory.getInstance());
		List<Integer> count = holder.takeCount();
		
		status.setRequest(count.get(0));
		status.setSuccess(count.get(1));
		status.setFail(count.get(2));
		
		status.setTotoalMemory(runtime.totalMemory());
		status.setFreeMemory(runtime.freeMemory());
		status.setMaxMemory(runtime.maxMemory());
		status.setUsedMemory(runtime.totalMemory() - runtime.freeMemory());
		
		return status;
	}
}
