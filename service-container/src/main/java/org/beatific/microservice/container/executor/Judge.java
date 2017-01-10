package org.beatific.microservice.container.executor;

import java.util.List;

import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.service.Service;

public interface Judge {

	public void cooldown(List<Instance> instances);
	public void entrust(Service service);
	public void take(String serviceName);
	public void clear();
}
