package org.beatific.microservice.container.instance;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.EurekaHttpClient;

@Component
public class InstanceLogicalManager {

	@Autowired
	private ApplicationContext context;

	@Autowired(required = false)
	private DiscoveryClientOptionalArgs optionalArgs;

	@Autowired
	private EurekaClientConfig config;
	
	@Autowired
	private ApplicationInfoManager manager;
	
	@Autowired
	private InstanceFinder finder;
	
	private EurekaHttpClient httpClient;

	@PostConstruct
	public void init() {

		DiscoveryClient discoveryClient = new CloudEurekaClient(manager, config, this.optionalArgs, this.context);

		Class<?> clss = discoveryClient.getClass();
		Class<?> klass = clss.getSuperclass();

		Field transField;
		try {

			transField = klass.getDeclaredField("eurekaTransport");
			transField.setAccessible(true);
			Object eurekaTransport = transField.get(discoveryClient);

			Class<?> klss = eurekaTransport.getClass();

			Field regiField = klss.getDeclaredField("registrationClient");
			regiField.setAccessible(true);
			httpClient = (EurekaHttpClient) regiField.get(eurekaTransport);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void unregister(String serviceName) {

		if (httpClient == null)
			return;

		finder.getAllInstaceInfos(serviceName).forEach((Instance instance) -> {
			httpClient.cancel(serviceName, instance.getInstanceId());
		});

	}

	public void unregister(Instance instance) {

		if (httpClient == null)
			return;

		httpClient.cancel(instance.getServiceName(), instance.getInstanceId());

	}
	
}
