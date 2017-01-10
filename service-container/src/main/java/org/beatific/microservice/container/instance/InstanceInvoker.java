package org.beatific.microservice.container.instance;

public interface InstanceInvoker {
	
	/**
	 * service 구동이 완료되면(discovery server 확인)  return해야 함
	 * @param serviceName
	 */
	public void invoke(String serviceName);
	
}
