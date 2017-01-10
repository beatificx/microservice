package org.beatific.microservice.container.executor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.OS;
import org.apache.commons.exec.PumpStreamHandler;
import org.beatific.microservice.container.instance.Instance;
import org.beatific.microservice.container.instance.InstanceFinder;
import org.beatific.microservice.container.instance.InstancePool;
import org.beatific.microservice.container.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.CloudEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.DiscoveryClient.DiscoveryClientOptionalArgs;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SyncWatchdog {
	
	@Autowired
	private ApplicationContext context;

	@Autowired(required = false)
	private DiscoveryClientOptionalArgs optionalArgs;
	
	@Autowired
	EurekaClientConfig config;
	
	@Autowired
	ApplicationInfoManager manager;
	
	@Autowired
	InstanceFinder finder;
	
	EurekaHttpClient httpClient;
	
	@PostConstruct
	public void init() {

		DiscoveryClient discoveryClient = new CloudEurekaClient(manager, config, this.optionalArgs, this.context);
		
		Class<?> clss = discoveryClient.getClass();
		Class<?> klass = clss.getSuperclass();
		log.error("clss name[{}]", klass.getName());

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

	public static boolean isProcessRunning(int pid, int timeout, TimeUnit timeunit) throws IOException {
		String line;
		if (OS.isFamilyWindows()) {
			// tasklist exit code is always 0. Parse output
			// findstr exit code 0 if found pid, 1 if it doesn't
			line = "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
		} else {
			// ps exit code 0 if process exists, 1 if it doesn't
			line = "ps -p " + pid;
			// `-p` is POSIX/BSD-compliant, `--pid`
			// isn't<ref>https://github.com/apache/storm/pull/296#discussion_r20535744</ref>
		}
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		// disable logging of stdout/strderr
		executor.setStreamHandler(new PumpStreamHandler(null, null, null));
		// disable exception for valid exit values
		executor.setExitValues(new int[] { 0, 1 });
		// set timer for zombie process
		ExecuteWatchdog timeoutWatchdog = new ExecuteWatchdog(timeunit.toMillis(timeout));
		executor.setWatchdog(timeoutWatchdog);
		int exitValue = executor.execute(cmdLine);
		// 0 is the default exit code which means the process exists
		return exitValue == 0;
	}

	public void watch(InstancePool instancePool) {
		
		log.error("instancePool {}", instancePool.get());
		
		for(Instance instance : instancePool.get()) {
			try {
				log.error("pid {}", instance.getPid());
				if (instance.getPid() > 0 && !isProcessRunning(instance.getPid(), 60, TimeUnit.SECONDS)) {
					log.error("unregister {}", instance);
					unregister(instance);
					instancePool.remove(instance);
				}
			} catch (IOException | RepositoryException e) {
				e.printStackTrace();
			}
		}
	}

	public void unregister(String serviceName) {

		if (httpClient == null)
			return;

		finder.getAllInstaceInfos(serviceName).forEach((Instance instance) -> {
			EurekaHttpResponse httpResponse = httpClient.cancel(serviceName, instance.getInstanceId());
		});
		
	}

	public void unregister(Instance instance) {

		if (httpClient == null)
			return;

		EurekaHttpResponse httpResponse = httpClient.cancel(instance.getServiceName(), instance.getInstanceId());

	}

}
