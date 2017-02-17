package org.beatific.microservice.container.monitor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.instance.InstanceCaster;
import org.beatific.microservice.container.machine.Machine;
import org.beatific.microservice.container.service.Service;
import org.beatific.microservice.container.utils.CopyUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestMonitor implements ServiceMonitor {

	private final String REQUEST_MONITOR = "/monitorRequest";
	private InstanceCaster<InstanceStatus> caster = new InstanceCaster<>(InstanceStatus.class);
	
	@Override
	public ServiceStatus monitor(Service service) {

		return monitor(service.getServiceName());
	}

	@Override
	public ServiceStatus monitor(String serviceName) {

		ServiceStatus status = new ServiceStatus();
		
		status.setServiceName(serviceName);
		List<InstanceStatus> instanceStatus = caster.multiCast(serviceName, REQUEST_MONITOR);
		log.debug("instanceStatus {}", instanceStatus);

		status.setRequest(instanceStatus.stream().mapToInt(x -> x.getRequest()).sum());
		status.setSuccess(instanceStatus.stream().mapToInt(x -> x.getSuccess()).sum());
		status.setFail(instanceStatus.stream().mapToInt(x -> x.getFail()).sum());
		status.setInstanceStatus(instanceStatus);
		return status;
	}

	@Override
	public ServiceStatus monitor(Instance instance) {
		ServiceStatus status = new ServiceStatus();

		status.setServiceName(instance.getServiceName());
		InstanceStatus instanceStatus = caster.cast(instance.getUri() + REQUEST_MONITOR);
		log.debug("instanceStatus {}", instanceStatus);

		status.setRequest(instanceStatus.getRequest());
		status.setSuccess(instanceStatus.getSuccess());
		status.setFail(instanceStatus.getFail());
		status.setInstanceStatus(Arrays.asList(instanceStatus));
		return status;
	}

	@Override
	public ServiceStatus monitor(List<Instance> instances) {
		ServiceStatus status = new ServiceStatus();

		String name = instances.stream().findAny().orElseGet(Instance::new).getServiceName();

		Objects.nonNull(name);

		if (instances.stream().filter(instance -> !name.equals(instance.getServiceName())).count() > 0)
			throw new MonitorException("Can not monitor instances of different services.");

		status.setServiceName(name);
		
		log.debug("name {}" , name);

		List<String> address = (List<String>) CopyUtils.copy(instances).stream().map(instance -> {
			return instance.getUri().toString();
		}).collect(Collectors.toList());
		
		log.debug("address {}" , address);
				
		List<InstanceStatus> instanceStatus = caster
				.multiCast(((List<String>) CopyUtils.copy(instances).stream().map(instance -> {
					return instance.getUri().toString();
				}).collect(Collectors.toList())), REQUEST_MONITOR);
		
		log.debug("instanceStatus {}", instanceStatus);

		status.setRequest(instanceStatus.stream().mapToInt(x -> x.getRequest()).sum());
		status.setSuccess(instanceStatus.stream().mapToInt(x -> x.getSuccess()).sum());
		status.setFail(instanceStatus.stream().mapToInt(x -> x.getFail()).sum());
		status.setInstanceStatus(instanceStatus);
		return status;
	}

	@Override
	public ServiceStatus monitor(Machine instance) {
		return null;
	}

	@Override
	public List<Instance> reduceInstances(String serviceName, Integer count) {
		ServiceStatus status = monitor(serviceName);
		List<InstanceStatus> istatus = Lists.partition(status.getInstanceStatus().stream().sorted((a,b) -> (int)(a.getUsedMemory() - b.getUsedMemory())).collect(Collectors.toList()), count).get(0);
		log.debug("istatus {}", istatus);
		return istatus.stream().map(i -> i.getInstance()).collect(Collectors.toList());
	}

}
